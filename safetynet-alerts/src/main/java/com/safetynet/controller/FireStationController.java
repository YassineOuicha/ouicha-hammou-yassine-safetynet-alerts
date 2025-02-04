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

@RequestMapping
@RestController
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/firestations")
    public List<FireStation> getAllFireStations(){
        return fireStationService.getAllFireStations();
    }

    @GetMapping
    public FireStation getFireStation(@RequestParam int station){
        return fireStationService.getFireStation(station)
                .orElseThrow(()-> new RuntimeException("Fire Station not found"));
    }

    @GetMapping("firestation")
    public PersonFireStationDTO getPersonsByFireStation(@RequestParam("stationNumber") int stationNumber){
        return fireStationService.getPersonsByFireStation(stationNumber);
    }

    @GetMapping("phoneAlert")
    public List<String> getPhoneNumbersByFireStation(@RequestParam("firestation") int firestationNumber){
        return fireStationService.getPhoneNumbersByFireStation(firestationNumber);
    }
    @GetMapping("fire")
    public FireDTO getPersonsByAddress(@RequestParam("address") String address){
        return fireStationService.getPersonsByAddress(address);
    }

    @GetMapping("/flood/stations")
    public Map<String, List<FireStationDTO>> getResidentsByStations(@RequestParam("stations") List<Integer> stationNumbers){
        return fireStationService.getResidentsByStations(stationNumbers);
    }

    @PostMapping("/add")
    public FireStation addFireStation(@RequestBody FireStation fireStation){
        return fireStationService.addFireStation(fireStation);
    }

    @PutMapping("/update")
    public boolean updateFireStation(@RequestBody FireStation fireStation){
        return fireStationService.updateFireStation(fireStation);
    }

    @DeleteMapping("/delete")
    public boolean deleteFireStation(@RequestBody FireStation fireStation){
        return fireStationService.deleteFireStation(fireStation);
    }

}
