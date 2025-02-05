package com.safetynet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object representing a child alert.
 * Contains the child's information such as name, age, and household members.
 */
@Data @Getter @Setter
public class ChildAlertDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<String> houseHoldMembers;

    // Setters and getters : the lombok annotations doesn't work correctly despite the dependency injection
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getHouseHoldMembers() {
        return houseHoldMembers;
    }

    public void setHouseHoldMembers(List<String> houseHoldMembers) {
        this.houseHoldMembers = houseHoldMembers;
    }
}
