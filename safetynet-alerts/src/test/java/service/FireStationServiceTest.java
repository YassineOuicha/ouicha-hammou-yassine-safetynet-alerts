package Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.safetynet.model.*;
import com.safetynet.repository.DataRepository;
import com.safetynet.service.BadRequestException;
import com.safetynet.service.DataService;
import com.safetynet.service.FireStationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FireStationServiceTest {

    private DataService dataService;
    private FireStationService fireStationService;
    private DataRepository dataRepository;

    @BeforeEach
    public void setUp() {

        dataService = mock(DataService.class);

        dataRepository = new DataRepository();

        List<FireStation> fireStations = new ArrayList<>();
        FireStation fs1 = new FireStation();
        fs1.setStation(1);
        fs1.setAddress("1509 Culver St");
        fireStations.add(fs1);
        dataRepository.setFirestations(fireStations);

        List<Person> persons = new ArrayList<>();
        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setAddress("1509 Culver St");
        person1.setPhone("841-874-6512");
        persons.add(person1);
        dataRepository.setPersons(persons);

        List<MedicalRecord> medicalRecords = new ArrayList<>();
        MedicalRecord mr1 = new MedicalRecord();
        mr1.setFirstName("John");
        mr1.setLastName("Doe");
        mr1.setBirthdate("03/06/1984");
        mr1.setMedications(Arrays.asList("aznol:350mg", "hydrapermazol:100mg"));
        mr1.setAllergies(Arrays.asList("nillacilan"));
        medicalRecords.add(mr1);

        dataRepository.setMedicalrecords(medicalRecords);

        when(dataService.getData()).thenReturn(dataRepository);

        fireStationService = new FireStationService(dataService);
    }

    @Test
    public void testGetAllFireStations() {
        List<FireStation> result = fireStationService.getAllFireStations();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1509 Culver St", result.get(0).getAddress());
    }

    @Test
    public void testGetPersonsByFireStation() {

        PersonFireStationDTO dto = fireStationService.getPersonsByFireStation(1);
        assertNotNull(dto);

        assertEquals(1, dto.getPersons().size());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthDate = LocalDate.parse("03/06/1984", formatter);
        int expectedAge = LocalDate.now().getYear() - birthDate.getYear();
        if (expectedAge > 18) {
            assertEquals(1, dto.getAdults());
            assertEquals(0, dto.getChildren());
        } else {
            assertEquals(0, dto.getAdults());
            assertEquals(1, dto.getChildren());
        }
    }

    @Test
    public void testGetPhoneNumbersByFireStation() {
        List<String> phones = fireStationService.getPhoneNumbersByFireStation(1);
        assertNotNull(phones);
        assertTrue(phones.contains("841-874-6512"));
        assertEquals(1, phones.size());
    }

    @Test
    public void testGetPersonsByAddress() {

        FireDTO fireDTO = fireStationService.getPersonsByAddress("1509 Culver St");
        assertNotNull(fireDTO);
        assertEquals(1, fireDTO.getStationNumber());
        assertFalse(fireDTO.getResidents().isEmpty());

        FireStationDTO resident = fireDTO.getResidents().get(0);

        assertEquals("John", resident.getFirstName());
        assertEquals("Doe", resident.getLastName());
        assertEquals("841-874-6512", resident.getPhone());
        assertTrue(resident.getAge() > 0);
        assertNotNull(resident.getMedications());
        assertNotNull(resident.getAllergies());
    }

    @Test
    public void testAddFireStation() {

        FireStation newFS = new FireStation();
        newFS.setStation(2);
        newFS.setAddress("29 15th St");

        FireStation addedFS = fireStationService.addFireStation(newFS);
        assertNotNull(addedFS);

        assertTrue(dataRepository.getFirestations().contains(newFS));

        Exception exception = assertThrows(BadRequestException.class, () -> {
            fireStationService.addFireStation(newFS);
        });
        assertTrue(exception.getMessage().contains("This FireStation already exists"));
    }

    @Test
    public void testUpdateFireStation() {

        FireStation updatedFS = new FireStation();
        updatedFS.setStation(1);
        updatedFS.setAddress("1509 Culver Street Updated");

        boolean result = fireStationService.updateFireStation(updatedFS);
        assertTrue(result);

        FireStation fs = dataRepository.getFirestations().stream()
                .filter(f -> f.getStation() == 1)
                .findFirst().orElse(null);
        assertNotNull(fs);
        assertEquals("1509 Culver Street Updated", fs.getAddress());
    }

    @Test
    public void testDeleteFireStation() {

        boolean deleted = fireStationService.deleteFireStation(dataRepository.getFirestations().get(0));
        assertTrue(deleted);

        assertTrue(dataRepository.getFirestations().isEmpty());
    }
}
