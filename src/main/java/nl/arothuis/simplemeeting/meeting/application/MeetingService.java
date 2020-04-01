package nl.arothuis.simplemeeting.meeting.application;

import nl.arothuis.simplemeeting.meeting.application.dto.MeetingDto;
import nl.arothuis.simplemeeting.meeting.application.dto.MeetingDtoTranslator;
import nl.arothuis.simplemeeting.meeting.application.dto.InviteeDto;
import nl.arothuis.simplemeeting.meeting.application.dto.RescheduleDto;
import nl.arothuis.simplemeeting.meeting.application.error.MeetingNotFound;
import nl.arothuis.simplemeeting.meeting.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MeetingService implements MeetingServiceInterface {
    private MeetingDtoTranslator translator;
    private MeetingDao dao;

    public MeetingService(
            MeetingDtoTranslator translator,
            MeetingDao meetingDao
    ) {
        this.translator = translator;
        this.dao = meetingDao;
    }

    @Override
    public void scheduleMeeting(MeetingDto dto) {
        Meeting meeting = this.translator.toMeeting(dto);
        this.dao.save(meeting);
    }

    @Override
    public List<MeetingDto> showMeeting(UUID meetingId) {
        MeetingDto dto = this.translator.toDto(
                this.getMeetingById(meetingId)
        );

        return List.of(dto);
    }

    @Override
    public void invite(InviteeDto invitee) {
        Meeting meeting = this.getMeetingById(UUID.fromString(invitee.meetingId));
        meeting.addInvitee(new Person(invitee.person));
        this.dao.save(meeting);
    }

    @Override
    public void respond(InviteeDto invitee) {
        Meeting meeting = this.getMeetingById(UUID.fromString(invitee.meetingId));
        meeting.registerResponse(new Person(invitee.person), Response.valueOf(invitee.response));
        this.dao.save(meeting);
    }

    @Override
    public void reschedule(RescheduleDto rescheduling) {
        Meeting meeting = this.getMeetingById(UUID.fromString(rescheduling.meetingId));

        TimeSlot newTimeSlot = this.translator.newTimeSlot(rescheduling.start, rescheduling.end);
        meeting.rescheduleTo(newTimeSlot);

        this.dao.save(meeting);
    }

    @Override
    public List<MeetingDto> listAll() {
        return this.dao.listAll()
                .stream()
                .map(this.translator::toDto)
                .collect(Collectors.toList());
    }

    private Meeting getMeetingById(UUID meetingId) {
        Optional<Meeting> optionalMeeting = this.dao.getById(meetingId);
        if (optionalMeeting.isEmpty()) {
            throw MeetingNotFound.withId(meetingId);
        }

        return optionalMeeting.get();
    }
}
