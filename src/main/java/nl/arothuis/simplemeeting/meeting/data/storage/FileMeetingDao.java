package nl.arothuis.simplemeeting.meeting.data.storage;

import nl.arothuis.simplemeeting.meeting.data.serialization.MeetingSerializer;
import nl.arothuis.simplemeeting.meeting.domain.Meeting;
import nl.arothuis.simplemeeting.meeting.domain.MeetingDao;
import nl.arothuis.simplemeeting.meeting.domain.error.CouldNotLoad;
import nl.arothuis.simplemeeting.meeting.domain.error.CouldNotSave;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileMeetingDao implements MeetingDao {
    private Path path;
    private MeetingDao cache;
    private MeetingSerializer serializer;

    public FileMeetingDao(Path path, MeetingDao cache, MeetingSerializer serializer) throws CouldNotLoad {
        this.path = path;
        this.cache = cache;
        this.serializer = serializer;

        this.fillCacheFromFile();
    }

    @Override
    public void save(Meeting meeting) throws CouldNotSave {
        this.cache.save(meeting);
        this.persist();
    }

    @Override
    public List<Meeting> listAll() throws CouldNotLoad {
        return this.cache.listAll();
    }

    @Override
    public Optional<Meeting> getById(UUID id) throws CouldNotLoad {
        return this.cache.getById(id);
    }

    @Override
    public void delete(Meeting meeting) throws CouldNotSave {
        this.cache.delete(meeting);
        this.persist();
    }

    private void fillCacheFromFile() throws CouldNotLoad {
        try {
            List<Meeting> meetings = this.loadFromDisk();
            for (Meeting meeting: meetings) {
                this.cache.save(meeting);
            }
        } catch (Exception e) {
            throw new CouldNotLoad(e);
        }
    }

    private List<Meeting> loadFromDisk() throws IOException {
        if (!Files.exists(this.path)) {
            return new ArrayList<>();
        }

        String meetingData = new String(Files.readAllBytes(this.path));
        return this.serializer.deserialize(meetingData);
    }

    private void persist() {
        try {
            List<Meeting> meetings = this.cache.listAll();
            String meetingData = this.serializer.serialize(meetings);
            Files.writeString(this.path, meetingData);
        } catch (Exception e) {
            throw new CouldNotSave(e);
        }
    }
}
