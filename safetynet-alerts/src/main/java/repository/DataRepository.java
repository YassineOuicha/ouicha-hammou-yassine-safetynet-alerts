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
public class DataRepository {

    private List<Person> persons;
    private List<MedicalRecord> medicalRecords;
    private List<FireStation> fireStations;

}
