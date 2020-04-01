package nl.arothuis.simplemeeting.meeting.domain.error;

import java.time.LocalDateTime;

public class InvalidTimeSlot extends RuntimeException {
    public InvalidTimeSlot(String message) {
        super(message);
    }

    public static InvalidTimeSlot wrongSequence(LocalDateTime start, LocalDateTime end) {
        return new InvalidTimeSlot(
                String.format(
                        "Invalid time slot: start (%s) cannot come after end (%s)",
                        start,
                        end
                )
        );
    }
}
