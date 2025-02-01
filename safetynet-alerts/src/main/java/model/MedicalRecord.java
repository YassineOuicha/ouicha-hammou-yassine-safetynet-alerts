package model;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class MedicalRecord {

    private String firstName;
    private String lastName;
    private String birthDate;
    private List<String> medications;
    private List<String> allergies;


}
