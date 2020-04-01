package nl.arothuis.simplemeeting.meeting.domain.error;

public class ResponseNotAllowed extends RuntimeException {
    public ResponseNotAllowed(String message) {
        super(message);
    }

    public static ResponseNotAllowed personCannotDeclineIfNotInvited() {
        return new ResponseNotAllowed("A person cannot decline an invitation they did not get");
    }

    public static ResponseNotAllowed personCannotAcceptIfNotInvited() {
        return new ResponseNotAllowed("A person cannot accept an invitation they did not get");
    }
}
