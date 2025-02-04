package com.safetynet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data @Getter @Setter
public class ChildAlertDTO {

    private String firstName;
    private String lastName;
    private int age;
    private List<String> houseHoldMembers;
}
