package repository;

import lombok.Data;
import lombok.Getter;
import model.FireStation;
import model.MedicalRecord;
import model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import service.DataService;

import java.util.List;


@Data
@Repository
public class DataRepository {

    private List<Person> persons;
    private List<MedicalRecord> medicalRecords;
    private List<FireStation> fireStations;

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<FireStation> getFireStations() {
        return fireStations;
    }
}
