package nl.arothuis.simplemeeting.meeting.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    @Test
    @DisplayName("is the same as another person based on value")
    public void sameValues() {
        Person person = new Person("person A");
        Person samePerson = new Person("person A");

        assertEquals(person.getName(), samePerson.getName());
        assertEquals(person, samePerson);
        assertEquals(person.hashCode(), samePerson.hashCode());
    }

    @Test
    @DisplayName("is different than another person based on value")
    public void differentValues() {
        Person person = new Person("person A");
        Person differentPerson = new Person("person B");

        assertNotEquals(person, differentPerson);
        assertNotEquals(person.hashCode(), differentPerson.hashCode());
    }
}
