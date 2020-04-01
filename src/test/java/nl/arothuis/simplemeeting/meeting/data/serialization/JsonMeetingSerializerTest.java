package nl.arothuis.simplemeeting.meeting.data.serialization;

import nl.arothuis.simplemeeting.meeting.MeetingProvider;
import nl.arothuis.simplemeeting.meeting.domain.Meeting;
import nl.arothuis.simplemeeting.meeting.domain.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JsonMeetingSerializerTest {
    @Test
    @DisplayName("cannot deserialize broken data")
    public void deserializingBrokenData() {
        JsonMeetingSerializer serializer = new JsonMeetingSerializer();

        assertThrows(
                SerializationException.class,
                () -> serializer.deserialize("{}")
        );
    }

    @Test
    @DisplayName("deserializes inverse to serializing")
    public void serializeAndDeserialize() {
        List<Meeting> meetings = List.of(
            MeetingProvider.provideMeeting(
                    "A",
                    List.of(
                        new Person("Person A"),
                        new Person("Person B")
                    )
            ),
            MeetingProvider.provideMeeting("B",
                    List.of(
                        new Person("Person C")
                    )
            )
        );

        JsonMeetingSerializer serializer = new JsonMeetingSerializer();
        List<Meeting> deserializedList = serializer.deserialize(
                serializer.serialize(meetings)
        );

        if (deserializedList.size() != meetings.size()) {
            fail("Meeting list should have same length after deserializing");
        }

        for (int i = 0; i < deserializedList.size(); i++) {
            Meeting meeting = meetings.get(i);
            Meeting deserialized = deserializedList.get(i);

            assertEquals(meeting.getId(), deserialized.getId());
            assertEquals(meeting.getDescription(), deserialized.getDescription());
            assertEquals(meeting.getTimeSlot(), deserialized.getTimeSlot());
            assertEquals(meeting.getInvitees(), deserialized.getInvitees());
        }
    }
}
