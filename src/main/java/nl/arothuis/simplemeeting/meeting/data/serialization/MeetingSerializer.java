package nl.arothuis.simplemeeting.meeting.data.serialization;

import nl.arothuis.simplemeeting.meeting.domain.Meeting;

import java.util.List;

public interface MeetingSerializer {
    String serialize(List<Meeting> meetings);
    List<Meeting> deserialize(String meetingsData);
}
