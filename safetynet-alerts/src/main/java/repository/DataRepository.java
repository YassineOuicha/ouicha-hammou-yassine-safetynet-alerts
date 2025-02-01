package repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import model.FireStation;
import model.MedicalRecord;
import model.Person;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

@Getter
@Repository
public class DataRepository {

    private List<Person> persons;
    private List<MedicalRecord> medicalRecords;
    private List<FireStation> fireStations;

    @PostConstruct
    public void LoadData(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file  = new File("ata.json");

            DataRepository data = objectMapper.readValue(file, DataRepository.class);
            this.persons = data.getPersons();
            this.fireStations = data.getFireStations();
            this.medicalRecords = data.getMedicalRecords();
            System.out.println("Data has been uploaded successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
