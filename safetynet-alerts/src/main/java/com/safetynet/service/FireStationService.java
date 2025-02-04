package com.safetynet.service;

import com.safetynet.model.FireStation;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import com.safetynet.model.PersonFireStationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.safetynet.repository.DataRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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
        return dataRepository.getFirestations();
    }

    public Optional<FireStation> getFireStation(int station){
        return getAllFireStations().stream().filter(fs -> (fs.getStation() == station)).findFirst();
    }

    public PersonFireStationDTO getPersonsByFireStation(int stationNumber) {

        FireStation fireStation = getAllFireStations().stream()
                .filter(fs -> fs.getStation() == stationNumber)
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Fire station wanted doesn't exits"));

        PersonFireStationDTO dto = new PersonFireStationDTO();

        List<Person> personsSameArea = dataService.getData().getPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(fireStation.getAddress()))
                .toList();

        dto.setPersons(personsSameArea);

        int nbAdults =0;
        int nbChildren =0;

        for(Person p: personsSameArea){
            MedicalRecord medicalRecord = dataService.getData().getMedicalrecords().stream()
                    .filter(mr-> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                    && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                    .findFirst()
                    .orElseThrow(()-> new RuntimeException("No medical record found for the person: "
                            + p.getFirstName() + " " + p.getLastName()));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);

            int age = calculateAge(birthDate);
            if(age>18){
                nbAdults++;
            } else {
                nbChildren++;
            }
        }
        dto.setAdults(nbAdults);
        dto.setChildren(nbChildren);
        return dto;
    }

    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        if(birthDate!=null){
            return Period.between(birthDate, currentDate).getYears();
        }
        return 0;
    }

    public List<String> getPhoneNumbersByFireStation(int fireStationNumber){
        List<String> addresses = getAllFireStations().stream()
                .filter(fs-> fs.getStation() == fireStationNumber).map(FireStation::getAddress).toList();

        return dataService.getData().getPersons().stream()
                                              .filter(p-> addresses.contains(p.getAddress()))
                                              .map(Person::getPhone)
                                              .distinct()
                                              .toList();
    }

    public getPersonsByAddress(String address){

    }

    public FireStation addFireStation(FireStation fireStation){
        Optional<FireStation> existingFireStation = getFireStation(fireStation.getStation());
        if(existingFireStation.isPresent()){
            throw new RuntimeException("This FireStation exists already");
        }
        dataService.getData().getFirestations().add(fireStation);
        return fireStation;
    }

    public boolean updateFireStation(FireStation updatedFireStation){
        FireStation oldFireStation = getFireStation(updatedFireStation.getStation())
                .orElseThrow(()-> new RuntimeException("Fire Station doesn't exists"));
        oldFireStation.setStation(updatedFireStation.getStation());
        oldFireStation.setAddress(updatedFireStation.getAddress());
        return true;
    }

    public boolean deleteFireStation(FireStation fireStation){
        return getAllFireStations().removeIf(fs -> fs.getStation() == fireStation.getStation());
    }

}
