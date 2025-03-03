package com.safetynet.service;

import com.safetynet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.safetynet.repository.DataRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service class responsible for managing and processing information related to fire stations and their residents.
 * Provides methods to get, add, update, and delete fire stations, as well as fetching information about the persons
 * in the areas covered by specific fire stations.
 */
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
                .orElseThrow(()-> new BadRequestException("Fire station wanted doesn't exist" + stationNumber));

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
                    .orElseThrow(()-> new BadRequestException("No medical record found for the person: "
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

    public FireDTO getPersonsByAddress(String address){

        FireDTO fireDTO = new FireDTO();

        int stationNumber = getAllFireStations().stream()
                            .filter(fs-> fs.getAddress().equalsIgnoreCase(address))
                            .map(FireStation::getStation)
                            .findFirst()
                            .orElseThrow(()-> new BadRequestException("No fire station found for this address" + address));

        fireDTO.setStationNumber(stationNumber);

        List<Person> personsSameArea = dataService.getData().getPersons().stream()
                                                 .filter(p-> p.getAddress().equalsIgnoreCase(address))
                                                 .toList();

        List<FireStationDTO> residents = new ArrayList<>();

        for(Person p: personsSameArea){
            FireStationDTO fireStationDTO = new FireStationDTO();

            fireStationDTO.setFirstName(p.getFirstName());
            fireStationDTO.setLastName(p.getLastName());
            fireStationDTO.setPhone(p.getPhone());

            MedicalRecord medicalRecord = dataService.getData().getMedicalrecords().stream()
                    .filter(mr-> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                            && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                    .findFirst()
                    .orElse(null);

            if (medicalRecord != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);

                int age = calculateAge(birthDate);

                fireStationDTO.setAge(age);
                fireStationDTO.setMedications(medicalRecord.getMedications());
                fireStationDTO.setAllergies(medicalRecord.getAllergies());

                residents.add(fireStationDTO);
            }
        }

        fireDTO.setResidents(residents);
        return fireDTO;
    }

    public Map<String, List<FireStationDTO>> getResidentsByStations(List<Integer> stationNumbers){

        Map<String, List<FireStationDTO>> residents = new HashMap<>();

        for(int stationNumber : stationNumbers){
            List<String> addresses = getAllFireStations().stream()
                    .filter(fs-> fs.getStation() == stationNumber)
                    .map(FireStation::getAddress)
                    .toList();

            for(String address: addresses){
                FireDTO fireDTO = getPersonsByAddress(address);
                residents.put(address, fireDTO.getResidents());
            }
        }
        return residents;
    }

    public FireStation addFireStation(FireStation fireStation){
        Optional<FireStation> existingFireStation = getFireStation(fireStation.getStation());
        if(existingFireStation.isPresent()){
            throw new BadRequestException( "This FireStation already exists");
        }
        dataService.getData().getFirestations().add(fireStation);
        return fireStation;
    }

    public boolean updateFireStation(FireStation updatedFireStation){
        FireStation oldFireStation = getFireStation(updatedFireStation.getStation())
                .orElseThrow(()-> new BadRequestException("Fire Station doesn't exist"));
        oldFireStation.setStation(updatedFireStation.getStation());
        oldFireStation.setAddress(updatedFireStation.getAddress());
        return true;
    }

    public boolean deleteFireStation(FireStation fireStation){
        return getAllFireStations().removeIf(fs -> fs.getStation() == fireStation.getStation());
    }

}
