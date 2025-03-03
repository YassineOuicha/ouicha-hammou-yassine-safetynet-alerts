package com.safetynet.controller;

import com.safetynet.model.MedicalRecord;
import com.safetynet.service.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.safetynet.service.MedicalRecordService;

import java.util.List;

/**
 * Controller for handling requests related to medical records.
 * Provides endpoints to retrieve, add, update, and delete medical record information.
 */
@RestController
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    /**
     * Endpoint to retrieve a list of all medical records.
     *
     * @return a list of all medical records
     */
    @GetMapping("/medicalrecords")
    public List<MedicalRecord> getAllMedicalRecords(){
        return medicalRecordService.getAllMedicalRecords();
    }

    /**
     * Endpoint to retrieve a medical record based on a combination of the first and the last name.
     *
     * @param firstName the first name of the person whose medical record is to be retrieved
     * @param lastName the last name of the person whose medical record is to be retrieved
     * @return the medical record of the person
     * @throws BadRequestException if the medical record is not found for the given name combination
     */
    @GetMapping("/medicalrecord")
    public MedicalRecord getMedicalRecord(@RequestParam String firstName, @RequestParam String lastName){
        return medicalRecordService.getMedicalRecord(firstName, lastName)
                .orElseThrow(()-> new BadRequestException("Medical record not found"));
    }

    /**
     * Endpoint to add a new medical record.
     *
     * @param medicalRecord the medical record information to be added
     * @return the added medical record information
     */
    @PostMapping("/medicalrecord")
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord){
        return medicalRecordService.addMedicalRecord(medicalRecord);
    }

    /**
     * Endpoint to update an existing medical record.
     *
     * @param medicalRecord the updated medical record information
     * @return true if the medical record was successfully updated, false otherwise
     */
    @PutMapping("/medicalrecord")
    public boolean updateMedicalRecord(@RequestBody MedicalRecord medicalRecord){
        return medicalRecordService.updateMedicalRecord(medicalRecord);
    }

    /**
     * Endpoint to delete a medical record based on a combination of the first and the last name.
     *
     * @param firstName the first name of the person whose medical record is to be deleted
     * @param lastName the last name of the person whose medical record is to be deleted
     * @return true if the medical record was successfully deleted, false otherwise
     */
    @DeleteMapping("/medicalrecord")
    public boolean deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName){
        MedicalRecord medicalRecord = getMedicalRecord(firstName, lastName);
        return medicalRecordService.deleteMedicalRecord(medicalRecord);
    }

}
