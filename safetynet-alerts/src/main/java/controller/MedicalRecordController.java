package controller;

import model.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.MedicalRecordService;

import java.util.List;

@RequestMapping("medicalrecord")
@RestController
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/all")
    public List<MedicalRecord> getAllMedicalRecords(){
        return medicalRecordService.getAllMedicalRecords();
    }

    @GetMapping
    public MedicalRecord getMedicalRecord(@RequestParam String firstName, @RequestParam String lastName){
        return medicalRecordService.getMedicalRecord(firstName, lastName)
                .orElseThrow(()-> new RuntimeException("Medical record not found"));
    }

    @PostMapping("/add")
    public MedicalRecord addMedicalRecord(@RequestBody MedicalRecord medicalRecord){
        return medicalRecordService.addMedicalRecord(medicalRecord);
    }

    @PutMapping("/update")
    public boolean updateMedicalRecord(@RequestBody MedicalRecord medicalRecord){
        return medicalRecordService.updateMedicalRecord(medicalRecord);
    }

    @DeleteMapping("/delete")
    public boolean deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName){
        MedicalRecord medicalRecord = getMedicalRecord(firstName, lastName);
        return medicalRecordService.deleteMedicalRecord(medicalRecord);
    }

}
