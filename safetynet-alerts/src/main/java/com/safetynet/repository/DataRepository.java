package com.safetynet.repository;

import lombok.Data;
import com.safetynet.model.FireStation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for managing and accessing lists of persons, fire stations,
 * and medical records.
 * This class acts as a data storage provider for the application.
 */
@Data
@Repository
public class DataRepository {

    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

    // getters : the lombok annotations doesn't work correctly despite the dependency injection
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
