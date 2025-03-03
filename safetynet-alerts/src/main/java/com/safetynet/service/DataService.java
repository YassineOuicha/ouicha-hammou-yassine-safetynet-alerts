package com.safetynet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.safetynet.repository.DataRepository;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;

/**
 * Service class responsible for loading and providing data from a JSON file.
 * This service reads data from a specified JSON file and stores data in the
 * DataRepository (persons, fire stations, and medical records).
 */
@Service
public class DataService {

    /**
     * The DataRepository instance that holds the loaded data.
     */
    private DataRepository dataRepository;

    /**
     * Resource file for data.json.
     */
    @Value("classpath:data.json")
    private Resource resourceFile;

    /**
     * Method that is executed after the construction of the beans. It loads data
     * from a data.json file and initializes the dataRepository.
     * The data is stored into the dataRepository using Jackson's ObjectMapper.
     */
    @PostConstruct
    public void LoadData(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            dataRepository = objectMapper.readValue(resourceFile.getFile(), DataRepository.class);
            System.out.println("JSON Data has been uploaded successfully");
        } catch (IOException e) {
            System.err.println("Error while loading data.json : " + e.getMessage());
        }
    }

    /**
     * Returns the dataRepository instance containing the loaded data.
     *
     * @return the dataRepository instance
     */
    public DataRepository getData(){
        return dataRepository;
    }


}
