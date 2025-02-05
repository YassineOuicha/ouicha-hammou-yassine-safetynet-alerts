package com.safetynet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.safetynet.repository.DataRepository;

import java.io.File;
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
     * Method that is executed after the construction of the beans. It loads data
     * from a data.json file and initializes the dataRepository.
     * The data is stored into the dataRepository using Jackson's ObjectMapper.
     */
    @PostConstruct
    public void LoadData(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file  = new File("C:/Users/User/OneDrive/Desktop/Y.Ouicha/OpenClassrooms/Projet_N5/ouicha-hammou-yassine-safetynet-alerts/safetynet-alerts/src/main/resources/data.json");
            dataRepository = objectMapper.readValue(file, DataRepository.class);
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
