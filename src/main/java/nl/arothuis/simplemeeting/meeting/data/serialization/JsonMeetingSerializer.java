package nl.arothuis.simplemeeting.meeting.data.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.arothuis.simplemeeting.meeting.domain.Meeting;
import nl.arothuis.simplemeeting.meeting.domain.Person;
import nl.arothuis.simplemeeting.meeting.domain.Response;
import nl.arothuis.simplemeeting.meeting.domain.TimeSlot;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JsonMeetingSerializer implements MeetingSerializer {
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private static ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String serialize(List<Meeting> meetings) {
        List<SerializableMeeting> serializable = meetings.stream()
                .map(this::toSerializableMeeting)
                .collect(Collectors.toList());

        try {
            return MAPPER.writeValueAsString(serializable);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public List<Meeting> deserialize(String meetingData) {
        try {
            List<SerializableMeeting> serializableList = MAPPER.readValue(meetingData, new TypeReference<>(){});
            return serializableList.stream()
                    .map(this::toMeeting)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private Meeting toMeeting(SerializableMeeting serializable) {
        Map<Person, Response> invitees = new HashMap<>();
        serializable.invitees.forEach(
                (person, response) ->
                        invitees.put(new Person(person), Response.valueOf(response))
        );

        TimeSlot timeSlot = new TimeSlot(
                LocalDateTime.parse(serializable.start, DATE_FORMATTER),
                LocalDateTime.parse(serializable.end, DATE_FORMATTER)
        );

        return Meeting.from(
                UUID.fromString(serializable.id),
                serializable.description,
                invitees,
                timeSlot
        );
    }

    private SerializableMeeting toSerializableMeeting(Meeting meeting) {
        var serializable = new SerializableMeeting();

        serializable.id = meeting.getId().toString();
        serializable.description = meeting.getDescription();
        serializable.start = meeting.getTimeSlot().getStart().format(DATE_FORMATTER);
        serializable.end = meeting.getTimeSlot().getEnd().format(DATE_FORMATTER);
        meeting.getInvitees().forEach((invitee, response) -> {
            serializable.invitees.put(invitee.getName(), response.toString());
        });

        return serializable;
    }
}
