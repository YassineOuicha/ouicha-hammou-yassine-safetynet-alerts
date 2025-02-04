package com.safetynet.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data @Setter @Getter
public class FireStationDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private int age;
    private List<String> medications;
    private List<String> allergies;

}
