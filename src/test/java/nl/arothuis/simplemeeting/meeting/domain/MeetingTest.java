package nl.arothuis.simplemeeting.meeting.domain;

import nl.arothuis.simplemeeting.meeting.MeetingProvider;
import nl.arothuis.simplemeeting.meeting.domain.error.ResponseNotAllowed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Meeting")
public class MeetingTest {
    private static final UUID SOME_UUID = UUID.randomUUID();
    private static final LocalDateTime JANUARY =
            LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0);
    private static final LocalDateTime FEBRUARY =
            LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0, 0);

    @Test
    @DisplayName("is created with a new id")
    void newMeeting() {
        String description = "some description";
        Person person = new Person("some person");
        Map<Person, Response> invitees = Map.of(person, Response.INVITED);
        TimeSlot timeSlot = new TimeSlot(JANUARY, FEBRUARY);

        Meeting meeting = Meeting.create(description, invitees, timeSlot);

        assertNotNull(meeting.getId());
        assertTrue(meeting.isInvited(person));
        assertSame(description, meeting.getDescription());
        assertEquals(timeSlot, meeting.getTimeSlot());
    }

    @Test
    @DisplayName("is created from an existing meeting")
    void existingMeeting() {
        String description = "some description";
        Person person = new Person("some person");
        TimeSlot timeSlot = new TimeSlot(JANUARY, FEBRUARY);

        Meeting meeting = MeetingProvider.provideMeeting(SOME_UUID, description, List.of(person), timeSlot);

        assertSame(SOME_UUID, meeting.getId());
        assertSame(description, meeting.getDescription());
        assertTrue(meeting.isInvited(person));
        assertEquals(timeSlot, meeting.getTimeSlot());
    }

    @Test
    @DisplayName("invitee not added")
    void inviteeNotAdded() {
        Person person = new Person("some person");
        Meeting meeting = this.provideMeeting(person);

        Person invitee = new Person("another person");
        assertFalse(meeting.isInvited(invitee));
    }

    @Test
    @DisplayName("add an invitee")
    void addInvitee() {
        Person person = new Person("some person");
        Meeting meeting = this.provideMeeting(person);

        Person invitee = new Person("another person");
        meeting.addInvitee(invitee);

        assertTrue(meeting.isInvited(invitee));
    }

    @Test
    @DisplayName("registers as invited by default")
    void invitedByDefault() {
        Person person = new Person("some person");
        Meeting meeting = this.provideMeeting(person);

        Person invitee = new Person("another person");
        meeting.registerResponse(invitee, Response.INVITED);

        assertTrue(meeting.isInvited(invitee));
    }

    @Test
    @DisplayName("cannot register acceptation from someone who was not invited")
    void cannotAcceptWhenNotInvited() {
        Meeting meeting = this.provideMeeting(new Person("invited"));
        assertThrows(ResponseNotAllowed.class, () ->
            meeting.registerResponse(new Person("uninvited"), Response.ACCEPTED)
        );
    }

    @Test
    @DisplayName("register acceptation")
    void accept() {
        Person person = new Person("invited");
        Meeting meeting = this.provideMeeting(person);

        meeting.addInvitee(person);
        meeting.registerResponse(person, Response.ACCEPTED);

        assertTrue(meeting.hasResponse(person, Response.ACCEPTED));
    }


    @Test
    @DisplayName("cannot register declination from someone who was not invited")
    void cannotDeclineWhenNotInvited() {
        Meeting meeting = this.provideMeeting(new Person("invited"));
        assertThrows(ResponseNotAllowed.class, () ->
                meeting.registerResponse(new Person("uninvited"), Response.DECLINED)
        );
    }

    @Test
    @DisplayName("register declination")
    void decline() {
        Person person = new Person("invited");
        Meeting meeting = this.provideMeeting(person);

        meeting.registerResponse(person, Response.DECLINED);

        assertTrue(meeting.hasResponse(person, Response.DECLINED));
    }

    @Test
    @DisplayName("reschedule to other timeslot")
    void rescheduling() {
        TimeSlot newTimeSlot = new TimeSlot(JANUARY, JANUARY);
        Meeting meeting = this.provideMeeting(new Person("some person"));

        meeting.rescheduleTo(newTimeSlot);

        assertSame(newTimeSlot, meeting.getTimeSlot());
    }

    @Test
    @DisplayName("reinvite everyone when rescheduled")
    void reinviteEveryoneWhenRescheduled() {
        TimeSlot newTimeSlot = new TimeSlot(JANUARY, JANUARY);
        Person person = new Person("some person");
        Meeting meeting = this.provideMeeting(person);
        meeting.addInvitee(new Person("another person"));

        meeting.registerResponse(new Person("some person"), Response.ACCEPTED);
        meeting.registerResponse(new Person("another person"), Response.DECLINED);
        meeting.rescheduleTo(newTimeSlot);

        Map<Person, Response> invitees = meeting.getInvitees();
        invitees.values().forEach(response -> assertSame(Response.INVITED, response));
    }

    private Meeting provideMeeting(Person person) {
        return MeetingProvider.provideMeeting(
                SOME_UUID,
                "some description",
                List.of(person),
                new TimeSlot(JANUARY, FEBRUARY)
        );
    }
}
