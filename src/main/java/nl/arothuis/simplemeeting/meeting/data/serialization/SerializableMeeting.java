package nl.arothuis.simplemeeting.meeting.data.serialization;

import java.util.HashMap;
import java.util.Map;

public class SerializableMeeting {
    public String id;
    public String description;
    public String start;
    public String end;
    public Map<String, String> invitees = new HashMap<>();
}
