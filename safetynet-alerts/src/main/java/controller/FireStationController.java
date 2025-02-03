package controller;

import model.FireStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.FireStationService;
import java.util.List;

@RequestMapping("/firestation")
@RestController
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    @GetMapping("/all")
    public List<FireStation> getAllFireStations(){
        return fireStationService.getAllFireStations();
    }

    @GetMapping
    public FireStation getFireStation(@RequestParam String station){
        return fireStationService.getFireStation(station)
                .orElseThrow(()-> new RuntimeException("Fire Station not found"));
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
