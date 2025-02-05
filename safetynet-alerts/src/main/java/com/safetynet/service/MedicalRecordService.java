package com.safetynet.service;


import com.safetynet.model.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.safetynet.repository.DataRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing medical records of persons.
 * Provides methods to get, add, update, and delete medical records.
 */
@Service
public class MedicalRecordService {

    private final DataService dataService;

    @Autowired
    public MedicalRecordService(DataService dataService) {
        this.dataService = dataService;
    }

    public List<MedicalRecord> getAllMedicalRecords(){
        DataRepository dataRepository = dataService.getData();
        return dataRepository.getMedicalrecords();
    }

    public Optional<MedicalRecord> getMedicalRecord(String firstName, String lastName){
        return getAllMedicalRecords().stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(firstName)
                && mr.getLastName().equalsIgnoreCase(lastName)).findFirst();
    }

    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord){
        Optional<MedicalRecord> existingMedicalRecord = getMedicalRecord(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if(existingMedicalRecord.isPresent()){
            throw new RuntimeException("This medical record exists already");
        }
        getAllMedicalRecords().add(medicalRecord);
        return medicalRecord;
    }

    public boolean updateMedicalRecord(MedicalRecord updatedMedicalRecord){
        MedicalRecord outDatedMedicalRecord = getMedicalRecord(updatedMedicalRecord.getFirstName(), updatedMedicalRecord.getLastName())
                .orElseThrow(()-> new RuntimeException("Medical record not found"));
        outDatedMedicalRecord.setMedications(updatedMedicalRecord.getMedications());
        outDatedMedicalRecord.setBirthdate(updatedMedicalRecord.getBirthdate());
        outDatedMedicalRecord.setAllergies(updatedMedicalRecord.getAllergies());
        return true;
    }

    public boolean deleteMedicalRecord(MedicalRecord medicalRecord){
        return getAllMedicalRecords().removeIf(mr -> mr.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName()) 
                                                                  && mr.getLastName().equalsIgnoreCase(medicalRecord.getLastName()));
    }

}
