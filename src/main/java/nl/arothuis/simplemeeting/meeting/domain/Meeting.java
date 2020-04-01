package nl.arothuis.simplemeeting.meeting.domain;

import nl.arothuis.simplemeeting.meeting.domain.error.ResponseNotAllowed;

import java.util.Map;
import java.util.UUID;

public class Meeting {
    private UUID id;
    private String description;
    private Map<Person, Response> invitees;
    private TimeSlot timeSlot;

    private Meeting(UUID id, String description, Map<Person, Response> invitees, TimeSlot timeSlot) {
        this.id = id;
        this.description = description;
        this.invitees = invitees;
        this.timeSlot = timeSlot;
    }

    public static Meeting from(UUID id, String description, Map<Person, Response> invitees, TimeSlot timeSlot) {
        return new Meeting(id, description, invitees, timeSlot);
    }

    public static Meeting create(String description, Map<Person, Response> invitees, TimeSlot timeSlot) {
        return new Meeting(UUID.randomUUID(), description, invitees, timeSlot);
    }

    public void addInvitee(Person person) {
        this.invitees.put(person, Response.INVITED);
    }

    public void registerResponse(Person person, Response response) {
        switch (response) {
            case DECLINED:
                this.registerDeclinationBy(person);
                break;
            case ACCEPTED:
                this.registerAcceptationBy(person);
                break;
            default:
                this.addInvitee(person);
                break;
        }
    }

    public void rescheduleTo(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
        this.reinviteEveryone();
    }

    public UUID getId() {
        return this.id;
    }

    public String getDescription() {
        return description;
    }

    public Map<Person, Response> getInvitees() {
        return invitees;
    }

    public boolean isInvited(Person person) {
        return this.invitees.containsKey(person);
    }

    public boolean hasResponse(Person person, Response response) {
        return this.isInvited(person)
                && this.invitees.get(person).equals(response);
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    private void registerDeclinationBy(Person person) {
        if (!this.isInvited(person)) {
            throw ResponseNotAllowed.personCannotDeclineIfNotInvited();
        }

        this.invitees.put(person, Response.DECLINED);
    }

    private void registerAcceptationBy(Person person) {
        if (!this.isInvited(person)) {
            throw ResponseNotAllowed.personCannotAcceptIfNotInvited();
        }

        this.invitees.put(person, Response.ACCEPTED);
    }

    private void reinviteEveryone() {
        this.invitees
                .keySet()
                .forEach(person -> this.invitees.put(person, Response.INVITED));
    }
}
