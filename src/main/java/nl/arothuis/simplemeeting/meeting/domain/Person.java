package nl.arothuis.simplemeeting.meeting.domain;

import java.util.Objects;

/**
 * Note that we override equals and hashCode as
 * a person is identified by value, rather than memory address
 *
 * This is relevant for how HashMaps compare keys, for example
 */
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
