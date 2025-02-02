package service;

import model.FireStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DataRepository;

import java.util.List;

@Service
public class FireStationService {

    @Autowired
    private DataService dataService;

    public List<FireStation> getAllFireStations(){
        DataRepository dataRepository = dataService.getData();

        return dataRepository.getFireStations();
    }

}
