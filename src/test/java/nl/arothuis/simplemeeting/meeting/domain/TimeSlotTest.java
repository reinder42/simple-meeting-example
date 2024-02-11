package nl.arothuis.simplemeeting.meeting.domain;

import nl.arothuis.simplemeeting.meeting.domain.error.InvalidTimeSlot;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Time slot")
public class TimeSlotTest {
    private static final LocalDateTime JANUARY =
            LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0);
    private static final LocalDateTime FEBRUARY =
            LocalDateTime.of(2020, Month.FEBRUARY, 1, 0, 0, 0);
    private static final LocalDateTime MARCH =
            LocalDateTime.of(2020, Month.MARCH, 1, 0, 0, 0);
    private static final LocalDateTime APRIL =
            LocalDateTime.of(2020, Month.APRIL, 1, 0, 0, 0);

    @Test
    @DisplayName("cannot be created if start comes after end")
    void noStartAfterEnd() {
        assertThrows(
                InvalidTimeSlot.class,
                () -> new TimeSlot(MARCH, FEBRUARY)
        );
    }

    @Test
    @DisplayName("is created successfully")
    void successfulCreation() {
        TimeSlot timeSlot = new TimeSlot(JANUARY, APRIL);
        assertSame(JANUARY, timeSlot.getStart());
        assertSame(APRIL, timeSlot.getEnd());
    }

    @Test
    @DisplayName("comes before a date")
    void  beforeADate() {
        TimeSlot timeSlot = new TimeSlot(JANUARY, MARCH);
        boolean isBefore = timeSlot.isBefore(APRIL);
        assertTrue(isBefore);
    }

    @Test
    @DisplayName("does not come before a date")
    void notBeforeADate() {
        TimeSlot timeSlot = new TimeSlot(JANUARY, MARCH);
        boolean isBefore = timeSlot.isBefore(JANUARY);
        assertFalse(isBefore);
    }

    @Test
    @DisplayName("comes after a date")
    void afterADate() {
        TimeSlot timeSlot = new TimeSlot(FEBRUARY, MARCH);
        boolean isAfter = timeSlot.isAfter(JANUARY);
        assertTrue(isAfter);
    }

    @Test
    @DisplayName("does not come after a date")
    void notAfterADate() {
        TimeSlot timeSlot = new TimeSlot(FEBRUARY, MARCH);
        boolean isAfter = timeSlot.isAfter(APRIL);
        assertFalse(isAfter);
    }

    @Test
    @DisplayName("is the same as another time slot")
    void sameTimeSlot() {
        TimeSlot timeSlot = new TimeSlot(FEBRUARY, MARCH);
        TimeSlot sameTimeSlot = new TimeSlot(FEBRUARY, MARCH);

        assertEquals(timeSlot, sameTimeSlot);
        assertEquals(timeSlot.hashCode(), sameTimeSlot.hashCode());
    }

    @Test
    @DisplayName("is not the same as another time slot")
    void notSameTimeSlot() {
        TimeSlot timeSlot = new TimeSlot(FEBRUARY, MARCH);
        TimeSlot differentTimeSlot = new TimeSlot(JANUARY, FEBRUARY);

        assertNotEquals(timeSlot, differentTimeSlot);
        assertNotEquals(timeSlot.hashCode(), differentTimeSlot.hashCode());
    }

    // !!!

    @Test
    @DisplayName("falls within another time slot")
    void inAnotherTimeSlot() {
        fail();
    }

    @Test
    @DisplayName("does not fall within another time slot")
    void notInAnotherTimeSlot() {
        fail();
    }

    @Test
    @DisplayName("falls within another time slot with the same start")
    void inAnotherTimeSlotWithSameStart() {
        fail();
    }

    @Test
    @DisplayName("falls within another time slot with the same end")
    void inAnotherTimeSlotWithSameEnd() {
        fail();
    }
}
