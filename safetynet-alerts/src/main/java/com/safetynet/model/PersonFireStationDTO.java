package com.safetynet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object representing the list of persons associated with
 * a particular fire station, along with the count of adults and children.
 */
@Setter
@Getter
@Data
public class PersonFireStationDTO {

    private List<Person> persons;
    private int adults;
    private int children;

    // Setters and getters : the lombok annotations doesn't work correctly despite the dependency injection
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }
}
