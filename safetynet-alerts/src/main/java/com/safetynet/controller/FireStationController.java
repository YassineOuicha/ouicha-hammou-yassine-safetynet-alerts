package com.safetynet.controller;

import com.safetynet.model.FireDTO;
import com.safetynet.model.FireStation;
import com.safetynet.model.FireStationDTO;
import com.safetynet.model.PersonFireStationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.safetynet.service.FireStationService;
import java.util.List;
import java.util.Map;

/**
 * Controller for handling requests related to fire stations.
 * Provides endpoints to retrieve information by criteria, get, add, update, and delete fire station information.
 */
@RestController
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    /**
     * Endpoint to retrieve a list of all fire stations.
     *
     * @return a list of all fire stations
     */
    @GetMapping("/firestations")
    public List<FireStation> getAllFireStations(){
        return fireStationService.getAllFireStations();
    }

    /**
     * Endpoint to retrieve information of a fire station by its station number.
     *
     * @param stationNumber the number of the fire station to retrieve
     * @return the fire station information
     * @throws RuntimeException if the fire station is not found
     */
    @GetMapping ("/firestation/{stationNumber}")
    public FireStation getFireStation(@RequestParam int stationNumber){
        return fireStationService.getFireStation(stationNumber)
                .orElseThrow(()-> new RuntimeException("Fire Station not found"));
    }

    /**
     * Endpoint to retrieve a list of persons associated with a specific fire station.
     *
     * @param stationNumber the number of the fire station to retrieve the persons for
     * @return a list of persons covered by this fire station, number of adults and number of children
     */
    @GetMapping("/firestation")
    public PersonFireStationDTO getPersonsByFireStation(@RequestParam("stationNumber") int stationNumber){
        return fireStationService.getPersonsByFireStation(stationNumber);
    }

    /**
     * Endpoint to retrieve phone numbers associated with a specific fire station
     * @param firestationNumber the number of the fire station
     * @return a list of phone numbers
     */
    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbersByFireStation(@RequestParam("firestation") int firestationNumber){
        return fireStationService.getPhoneNumbersByFireStation(firestationNumber);
    }

    /**
     * Endpoint to retrieve persons associated with a specific address.
     * @param address the address to retrieve the persons for
     * @return a map where the key is fire station number and the value is a list of residents
     */
    @GetMapping("/fire")
    public FireDTO getPersonsByAddress(@RequestParam("address") String address){
        return fireStationService.getPersonsByAddress(address);
    }

    /**
     * Endpoint to retrieve a map of residents by fire station numbers for flood alerts.
     *
     * @param stationNumbers a list of fire station numbers to retrieve residents for
     * @return a map where the key is the station number and the value is a list of residents
     */
    @GetMapping("/flood/stations")
    public Map<String, List<FireStationDTO>> getResidentsByStations(@RequestParam("stations") List<Integer> stationNumbers){
        return fireStationService.getResidentsByStations(stationNumbers);
    }

    /**
     * Endpoint to add a new fire station.
     *
     * @param fireStation the fire station information to be added
     * @return the added fire station information
     */
    @PostMapping("/firestation")
    public FireStation addFireStation(@RequestBody FireStation fireStation){
        return fireStationService.addFireStation(fireStation);
    }

    /**
     * Endpoint to update an existing fire station.
     *
     * @param fireStation the updated fire station information
     * @return true if the fire station was successfully updated, false otherwise
     */
    @PutMapping("/firestation")
    public boolean updateFireStation(@RequestBody FireStation fireStation){
        return fireStationService.updateFireStation(fireStation);
    }

    /**
     * Endpoint to delete a fire station.
     *
     * @param fireStation the fire station to be deleted
     * @return true if the fire station was successfully deleted, false otherwise
     */
    @DeleteMapping("/firestation")
    public boolean deleteFireStation(@RequestBody FireStation fireStation){
        return fireStationService.deleteFireStation(fireStation);
    }

}
