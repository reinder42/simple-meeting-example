package nl.arothuis.simplemeeting.meeting.data.storage;

import nl.arothuis.simplemeeting.meeting.domain.Meeting;
import nl.arothuis.simplemeeting.meeting.domain.MeetingDao;

import java.util.*;

public class InMemoryMeetingDao implements MeetingDao {
    private Map<UUID, Meeting> meetings = new HashMap<>();

    @Override
    public void save(Meeting meeting) {
        this.meetings.put(meeting.getId(), meeting);
    }

    @Override
    public List<Meeting> listAll() {
        return new ArrayList<>(this.meetings.values());
    }

    @Override
    public Optional<Meeting> getById(UUID id) {
        if (!this.meetings.containsKey(id)) {
            return Optional.empty();
        }

        return Optional.of(this.meetings.get(id));
    }

    @Override
    public void delete(Meeting meeting) {
        this.meetings.remove(meeting.getId());
    }
}
