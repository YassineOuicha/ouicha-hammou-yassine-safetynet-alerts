package service;

import model.FireStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DataRepository;

import java.util.List;

@Service
public class FireStationService {

    @Autowired
    private DataRepository dataRepository;

    public List<FireStation> getAllFireStations(){
        return dataRepository.getFireStations();
    }

}
