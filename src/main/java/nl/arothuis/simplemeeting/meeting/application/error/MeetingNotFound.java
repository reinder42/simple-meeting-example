package nl.arothuis.simplemeeting.meeting.application.error;

import java.util.UUID;

public class MeetingNotFound extends RuntimeException {
    public MeetingNotFound(String message) {
        super(message);
    }


    public static MeetingNotFound withId(UUID meetingId) {
        return new MeetingNotFound("Meeting not found: " + meetingId.toString());
    }
}
