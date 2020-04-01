package nl.arothuis.simplemeeting.meeting.application.dto;

import javax.validation.constraints.NotEmpty;

public class RescheduleDto {
    public String meetingId;

    @NotEmpty
    public String start;

    @NotEmpty
    public String end;
}
