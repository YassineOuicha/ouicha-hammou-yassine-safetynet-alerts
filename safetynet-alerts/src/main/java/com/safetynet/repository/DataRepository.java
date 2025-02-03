package com.safetynet.repository;

import lombok.Data;
import com.safetynet.model.FireStation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;


@Data
@Repository
public class DataRepository {

    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

    public List<Person> getPersons() {
        return persons;
    }

    public List<FireStation> getFirestations() {
        return firestations;
    }

    public List<MedicalRecord> getMedicalrecords() {
        return medicalrecords;
    }
}
