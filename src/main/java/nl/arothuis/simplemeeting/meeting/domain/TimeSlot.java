package nl.arothuis.simplemeeting.meeting.domain;

import nl.arothuis.simplemeeting.meeting.domain.error.InvalidTimeSlot;

import java.time.LocalDateTime;
import java.util.Objects;

public class TimeSlot {
    private LocalDateTime start;
    private LocalDateTime end;

    public TimeSlot(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw InvalidTimeSlot.wrongSequence(start, end);
        }

        this.start = start;
        this.end = end;
    }

    public boolean isBefore(LocalDateTime date) {
        return this.end.isBefore(date);
    }

    public boolean isAfter(LocalDateTime date) {
        return this.start.isAfter(date);
    }

    public boolean isWithin(TimeSlot other) {
        return true;
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(start, timeSlot.start) &&
                Objects.equals(end, timeSlot.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
