package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import repository.DataRepository;

import java.io.File;
import java.io.IOException;

@Service
public class DataService {

    private DataRepository dataRepository;

    @PostConstruct
    public void LoadData(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file  = new File("src/main/resources/data.json");
            dataRepository = objectMapper.readValue(file, DataRepository.class);
            System.out.println("JSON Data has been uploaded successfully");
        } catch (IOException e) {
            System.err.println("Error while loading data.json : " + e.getMessage());
        }
    }

    public DataRepository getData(){
        return dataRepository;
    }


}
