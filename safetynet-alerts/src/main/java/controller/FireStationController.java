package controller;

import model.FireStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.FireStationService;

import java.util.List;

@RequestMapping("/firestation")
@RestController
public class FireStationController {

    @Autowired
    private FireStationService fireStationService;

    @GetMapping
    public List<FireStation> getAllFireStations(){
        return fireStationService.getAllFireStations();
    }

}
