package Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.safetynet.model.MedicalRecord;
import com.safetynet.repository.DataRepository;
import com.safetynet.service.BadRequestException;
import com.safetynet.service.DataService;
import com.safetynet.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    private DataService dataService;
    private DataRepository dataRepository;
    private MedicalRecordService medicalRecordService;

    @BeforeEach
    public void setUp() {

        dataService = mock(DataService.class);
        dataRepository = new DataRepository();

        List<MedicalRecord> medicalRecords = new ArrayList<>();
        MedicalRecord mr1 = new MedicalRecord();
        mr1.setFirstName("John");
        mr1.setLastName("Doe");
        mr1.setBirthdate("03/06/1984");
        mr1.setMedications(Arrays.asList("aznol:350mg"));
        mr1.setAllergies(Arrays.asList("nillacilan"));
        medicalRecords.add(mr1);
        dataRepository.setMedicalrecords(medicalRecords);

        when(dataService.getData()).thenReturn(dataRepository);

        medicalRecordService = new MedicalRecordService(dataService);
    }

    @Test
    public void testGetAllMedicalRecords() {
        List<MedicalRecord> records = medicalRecordService.getAllMedicalRecords();
        assertNotNull(records);
        assertEquals(1, records.size());
    }

    @Test
    public void testGetMedicalRecordFound() {
        Optional<MedicalRecord> record = medicalRecordService.getMedicalRecord("John", "Doe");
        assertTrue(record.isPresent());
        assertEquals("John", record.get().getFirstName());
    }

    @Test
    public void testGetMedicalRecordNotFound() {
        Optional<MedicalRecord> record = medicalRecordService.getMedicalRecord("Jane", "Doe");
        assertFalse(record.isPresent());
    }

    @Test
    public void testAddMedicalRecordSuccess() {

        MedicalRecord mr2 = new MedicalRecord();
        mr2.setFirstName("Jane");
        mr2.setLastName("Doe");
        mr2.setBirthdate("05/10/1990");
        mr2.setMedications(Arrays.asList("ibuprofen:200mg"));
        mr2.setAllergies(Arrays.asList("pollen"));

        MedicalRecord addedRecord = medicalRecordService.addMedicalRecord(mr2);

        assertNotNull(addedRecord);
        assertEquals("Jane", addedRecord.getFirstName());
        assertEquals(2, dataRepository.getMedicalrecords().size());
    }

    @Test
    public void testAddMedicalRecordAlreadyExists() {

        MedicalRecord duplicate = new MedicalRecord();
        duplicate.setFirstName("John");
        duplicate.setLastName("Doe");
        duplicate.setBirthdate("03/06/1984");
        duplicate.setMedications(Arrays.asList("aznol:350mg"));
        duplicate.setAllergies(Arrays.asList("nillacilan"));

        Exception exception = assertThrows(BadRequestException.class, () -> {
            medicalRecordService.addMedicalRecord(duplicate);
        });
        assertTrue(exception.getMessage().contains("This medical record already exists"));
    }

    @Test
    public void testUpdateMedicalRecordSuccess() {

        MedicalRecord update = new MedicalRecord();
        update.setFirstName("John");
        update.setLastName("Doe");
        update.setBirthdate("04/04/1985");
        update.setMedications(Arrays.asList("newMed:100mg"));
        update.setAllergies(Arrays.asList("newAllergy"));

        boolean updated = medicalRecordService.updateMedicalRecord(update);
        assertTrue(updated);

        Optional<MedicalRecord> recordOptional = medicalRecordService.getMedicalRecord("John", "Doe");
        assertTrue(recordOptional.isPresent());
        MedicalRecord record = recordOptional.get();
        assertEquals("04/04/1985", record.getBirthdate());
        assertEquals(Arrays.asList("newMed:100mg"), record.getMedications());
        assertEquals(Arrays.asList("newAllergy"), record.getAllergies());
    }

    @Test
    public void testUpdateMedicalRecordNotFound() {

        MedicalRecord update = new MedicalRecord();
        update.setFirstName("Alice");
        update.setLastName("Smith");
        update.setBirthdate("05/05/1990");
        update.setMedications(Arrays.asList("med:50mg"));
        update.setAllergies(Arrays.asList("none"));

        Exception exception = assertThrows(BadRequestException.class, () -> {
            medicalRecordService.updateMedicalRecord(update);
        });
        assertTrue(exception.getMessage().contains("Medical record not found"));
    }

    @Test
    public void testDeleteMedicalRecordSuccess() {

        MedicalRecord mrToDelete = new MedicalRecord();
        mrToDelete.setFirstName("John");
        mrToDelete.setLastName("Doe");
        boolean deleted = medicalRecordService.deleteMedicalRecord(mrToDelete);
        assertTrue(deleted);

        assertFalse(medicalRecordService.getMedicalRecord("John", "Doe").isPresent());
    }

    @Test
    public void testDeleteMedicalRecordNotFound() {

        MedicalRecord nonExisting = new MedicalRecord();
        nonExisting.setFirstName("Bob");
        nonExisting.setLastName("Marley");
        boolean deleted = medicalRecordService.deleteMedicalRecord(nonExisting);
        assertFalse(deleted);
    }

}
