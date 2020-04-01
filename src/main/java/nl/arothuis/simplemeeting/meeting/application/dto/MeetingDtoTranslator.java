package nl.arothuis.simplemeeting.meeting.application.dto;

import nl.arothuis.simplemeeting.meeting.domain.Meeting;
import nl.arothuis.simplemeeting.meeting.domain.Person;
import nl.arothuis.simplemeeting.meeting.domain.Response;
import nl.arothuis.simplemeeting.meeting.domain.TimeSlot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MeetingDtoTranslator {
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public MeetingDto toDto(Meeting meeting) {
        var dto = new MeetingDto();

        dto.id = meeting.getId().toString();
        dto.description = meeting.getDescription();
        meeting.getInvitees().forEach(
                (person, response) ->
                        dto.invitees.put(person.getName(), response.name())
        );
        dto.start = meeting.getTimeSlot().getStart().format(DATE_FORMATTER);
        dto.end = meeting.getTimeSlot().getEnd().format(DATE_FORMATTER);

        return dto;
    }

    public Meeting toMeeting(MeetingDto dto) {
        Map<Person, Response> invitees = new HashMap<>();
        dto.invitees.forEach(
                (person, response) ->
                        invitees.put(new Person(person), Response.valueOf(response))
        );

        TimeSlot timeSlot = this.newTimeSlot(dto.start, dto.end);

        if (dto.id == null) {
            return Meeting.create(dto.description, invitees, timeSlot);
        }

        return Meeting.from(
                UUID.fromString(dto.id),
                dto.description,
                invitees,
                timeSlot
        );
    }

    public TimeSlot newTimeSlot(String start, String end) {
        return new TimeSlot(
                LocalDateTime.parse(start, DATE_FORMATTER),
                LocalDateTime.parse(end, DATE_FORMATTER)
        );
    }
}
