package nl.arothuis.simplemeeting.meeting.data.storage;

import nl.arothuis.simplemeeting.meeting.MeetingProvider;
import nl.arothuis.simplemeeting.meeting.domain.Meeting;
import nl.arothuis.simplemeeting.meeting.domain.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("In-Memory Meeting DAO")
public class InMemoryMeetingDaoTest {
    private static Meeting MEETING_A = MeetingProvider.provideMeeting(
            "meeting A",
            List.of(new Person("Person A"), new Person("Person B"))
    );
    private static Meeting MEETING_B = MeetingProvider.provideMeeting(
            "meeting B",
            List.of(new Person("Person C"), new Person("Person D"))
    );

    @Test
    @DisplayName("stores and loads meetings")
    void storesAndLoadsMeetings() {
        InMemoryMeetingDao dao = new InMemoryMeetingDao();

        dao.save(MEETING_A);
        dao.save(MEETING_B);

        List<Meeting> meetings = dao.listAll();
        assertEquals(2, meetings.size());
        assertTrue(meetings.contains(MEETING_A));
        assertTrue(meetings.contains(MEETING_B));
    }

    @Test
    @DisplayName("get an existing meeting")
    public void getExistingMeeting() {
        InMemoryMeetingDao dao = new InMemoryMeetingDao();

        dao.save(MEETING_A);

        Optional<Meeting> result = dao.getById(MEETING_A.getId());
        result.ifPresentOrElse(
                (meetingResult) -> assertEquals(MEETING_A, meetingResult),
                () -> fail("Meeting should have been present")
        );
    }

    @Test
    @DisplayName("cannot get a non-existent meeting")
    public void cannotGetNonExistentMeeting() {
        InMemoryMeetingDao dao = new InMemoryMeetingDao();
        Optional<Meeting> result = dao.getById(MEETING_A.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deletes a meeting")
    public void deleteMeeting() {
        InMemoryMeetingDao dao = new InMemoryMeetingDao();
        dao.save(MEETING_A);

        dao.delete(MEETING_A);
        Optional<Meeting> result = dao.getById(MEETING_A.getId());

        assertFalse(result.isPresent());
    }
}
