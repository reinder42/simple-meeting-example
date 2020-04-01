package nl.arothuis.simplemeeting.meeting.domain.error;

public class CouldNotLoad extends RuntimeException {
    public CouldNotLoad(Throwable cause) {
        super(cause);
    }
}
