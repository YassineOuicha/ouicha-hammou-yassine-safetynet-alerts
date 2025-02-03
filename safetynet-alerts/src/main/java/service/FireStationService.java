package service;

import model.FireStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DataRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FireStationService {

    @Autowired
    private final DataService dataService;

    public FireStationService(DataService dataService) {
        this.dataService = dataService;
    }

    public List<FireStation> getAllFireStations(){
        DataRepository dataRepository = dataService.getData();
        return dataRepository.getFireStations();
    }

    public Optional<FireStation> getFireStation(int station){
        return getAllFireStations().stream().filter(f -> (f.getStation() == station)).findFirst();
    }

    public FireStation addFireStation(FireStation fireStation){
        Optional<FireStation> existingFireStation = getFireStation(fireStation.getStation());
        if(existingFireStation.isPresent()){
            throw new RuntimeException("This FireStation exists already");
        }
        dataService.getData().getFireStations().add(fireStation);
        return fireStation;
    }

    public void upDateFireStation(FireStation updatedFireStation){

        FireStation oldFireStation = getFireStation(updatedFireStation.getStation())
                .orElseThrow(()-> new RuntimeException("Fire Station doesn't exists"));
        oldFireStation.setStation(updatedFireStation.getStation());
        oldFireStation.setAddress(updatedFireStation.getAddress());
    }

    public boolean deleteFireStation(FireStation fireStation){
        return getAllFireStations().removeIf(f -> f.getStation() == fireStation.getStation());
    }

}
