package nl.arothuis.simplemeeting.meeting;

import nl.arothuis.simplemeeting.meeting.domain.Meeting;
import nl.arothuis.simplemeeting.meeting.domain.Person;
import nl.arothuis.simplemeeting.meeting.domain.Response;
import nl.arothuis.simplemeeting.meeting.domain.TimeSlot;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Static factory easing the creation of meetings
 * for testing purposes.
 */
public class MeetingProvider {
    public static Meeting provideMeeting(UUID id, String description, List<Person> people, TimeSlot timeSlot) {
        Map<Person, Response> invitees = new HashMap<>();
        people.forEach(person -> invitees.put(person, Response.INVITED));
        return Meeting.from(id, description, invitees, timeSlot);
    }

    public static Meeting provideMeeting(String description, List<Person> people) {
        TimeSlot timeSlot = new TimeSlot(
                LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0),
                LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0, 0)
        );

        return provideMeeting(UUID.randomUUID(), description, people, timeSlot);
    }
}
