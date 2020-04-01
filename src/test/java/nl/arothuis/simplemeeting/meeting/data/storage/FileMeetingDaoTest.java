package nl.arothuis.simplemeeting.meeting.data.storage;

import nl.arothuis.simplemeeting.meeting.MeetingProvider;
import nl.arothuis.simplemeeting.meeting.data.serialization.JsonMeetingSerializer;
import nl.arothuis.simplemeeting.meeting.domain.Meeting;
import nl.arothuis.simplemeeting.meeting.domain.MeetingDao;
import nl.arothuis.simplemeeting.meeting.domain.Person;

import nl.arothuis.simplemeeting.meeting.domain.error.CouldNotLoad;
import nl.arothuis.simplemeeting.meeting.domain.error.CouldNotSave;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("integration")
public class FileMeetingDaoTest {
    private static Meeting MEETING_A = MeetingProvider.provideMeeting(
            "meeting A",
            List.of(new Person("Person A"), new Person("Person B"))
    );
    private static Meeting MEETING_B = MeetingProvider.provideMeeting(
            "meeting B",
            List.of(new Person("Person C"), new Person("Person D"))
    );

    private Path testFile = Path.of("src/test/resources/temp.json");
    private FileMeetingDao fileDao;

    @BeforeEach
    void beforeEachTest() throws IOException {
        // Start every test case with a clean file
        // as we don't want tests interfering with each other!
        Files.deleteIfExists(this.testFile);

        this.fileDao = new FileMeetingDao(
                this.testFile,
                new InMemoryMeetingDao(),
                new JsonMeetingSerializer()
        );
    }

    @AfterEach
    void afterEachTest() throws IOException {
        // Clean up used test file
        Files.deleteIfExists(this.testFile);
    }

    @Test
    @DisplayName("stores and loads meetings")
    void storesAndLoadsMeetings() {
        this.fileDao.save(MEETING_A);
        this.fileDao.save(MEETING_B);
        List<Meeting> meetings = this.fileDao.listAll();

        assertEquals(2, meetings.size());
        assertTrue(meetings.contains(MEETING_A));
        assertTrue(meetings.contains(MEETING_B));
    }

    @Test
    @DisplayName("get an existing meeting")
    void getExistingMeeting() {
        this.fileDao.save(MEETING_A);

        Optional<Meeting> result = this.fileDao.getById(MEETING_A.getId());
        result.ifPresentOrElse(
                (meetingResult) -> Assertions.assertEquals(MEETING_A, meetingResult),
                () -> fail("Meeting should have been present")
        );
    }

    @Test
    @DisplayName("cannot get a non-existent meeting")
    void cannotGetNonExistentMeeting() {
        Optional<Meeting> result = this.fileDao.getById(MEETING_A.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("deletes a meeting")
    void deleteMeeting() {
        this.fileDao.save(MEETING_A);

        this.fileDao.delete(MEETING_A);
        Optional<Meeting> result = this.fileDao.getById(MEETING_A.getId());

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("saves a meeting and persists")
    void saveAndPersist() {
        // Old dao persists through empty file
        this.fileDao.save(MEETING_A);
        this.fileDao.save(MEETING_B);

        // New dao should load from file
        FileMeetingDao newDao = new FileMeetingDao(
                this.testFile,
                new InMemoryMeetingDao(),
                new JsonMeetingSerializer()
        );
        List<Meeting> meetings = newDao.listAll();

        assertFalse(meetings.isEmpty());
        assertEquals(2, meetings.size());
    }

    @Test
    @DisplayName("deletes a meeting and persists")
    void deleteMeetingAndPersist() {
        this.fileDao.save(MEETING_A);

        this.fileDao.delete(MEETING_A);
        FileMeetingDao newDao = new FileMeetingDao(
                this.testFile,
                new InMemoryMeetingDao(),
                new JsonMeetingSerializer()
        );
        Optional<Meeting> result = newDao.getById(MEETING_A.getId());

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("cannot load from invalid file upon creation")
    void loadFromInvalidFile() throws IOException {
        Files.writeString(this.testFile, "<INVALID JSON>");

        assertThrows(
            CouldNotLoad.class,
            () -> new FileMeetingDao(
                    this.testFile,
                    new InMemoryMeetingDao(),
                    new JsonMeetingSerializer()
            )
        );
    }

    @Test
    @DisplayName("cannot save if cache fails")
    void cannotSaveOnCacheFailure() {
        // Create test double using Mockito
        MeetingDao mockCache = mock(MeetingDao.class);
        FileMeetingDao dao = new FileMeetingDao(
                this.testFile,
                mockCache,
                new JsonMeetingSerializer()
        );

        // Configure mock to throw when called
        doThrow(new RuntimeException("OOPS!"))
            .when(mockCache).listAll();

        assertThrows(
                CouldNotSave.class,
                () -> dao.save(MEETING_A)
        );
    }
}
