package Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.safetynet.model.ChildAlertDTO;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import com.safetynet.model.PersonInfoDTO;
import com.safetynet.repository.DataRepository;
import com.safetynet.service.BadRequestException;
import com.safetynet.service.DataService;
import com.safetynet.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    private DataService dataService;
    private DataRepository dataRepository;
    private PersonService personService;

    @BeforeEach
    public void setUp() {

        dataService = mock(DataService.class);
        dataRepository = new DataRepository();

        List<Person> persons = new ArrayList<>();

        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setAddress("1509 Culver St");
        person1.setCity("Culver");
        person1.setEmail("john.doe@example.com");
        person1.setPhone("841-874-6512");
        persons.add(person1);

        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setAddress("1509 Culver St");
        person2.setCity("Culver");
        person2.setEmail("jane.doe@example.com");
        person2.setPhone("841-874-6513");
        persons.add(person2);

        Person person3 = new Person();
        person3.setFirstName("Bob");
        person3.setLastName("Smith");
        person3.setAddress("29 15th St");
        person3.setCity("Culver");
        person3.setEmail("bob.smith@example.com");
        person3.setPhone("841-874-6514");
        persons.add(person3);

        dataRepository.setPersons(persons);

        List<MedicalRecord> medicalRecords = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        MedicalRecord mr1 = new MedicalRecord();
        mr1.setFirstName("John");
        mr1.setLastName("Doe");
        mr1.setBirthdate("03/06/1984");
        mr1.setMedications(Arrays.asList("aznol:350mg"));
        mr1.setAllergies(Arrays.asList("nillacilan"));
        medicalRecords.add(mr1);

        MedicalRecord mr2 = new MedicalRecord();
        mr2.setFirstName("Jane");
        mr2.setLastName("Doe");
        mr2.setBirthdate("03/06/2015");
        mr2.setMedications(Arrays.asList("ibuprofen:200mg"));
        mr2.setAllergies(Arrays.asList("pollen"));
        medicalRecords.add(mr2);

        MedicalRecord mr3 = new MedicalRecord();
        mr3.setFirstName("Bob");
        mr3.setLastName("Smith");
        mr3.setBirthdate("07/15/1975");
        mr3.setMedications(Arrays.asList("aspirin:100mg"));
        mr3.setAllergies(Arrays.asList("nuts"));
        medicalRecords.add(mr3);

        dataRepository.setMedicalrecords(medicalRecords);

        when(dataService.getData()).thenReturn(dataRepository);

        personService = new PersonService(dataService);
    }

    @Test
    public void testGetAllPersons() {
        List<Person> result = personService.getAllPersons();
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    public void testGetPersonFound() {
        Optional<Person> person = personService.getPerson("John", "Doe");
        assertTrue(person.isPresent());
        assertEquals("John", person.get().getFirstName());
    }

    @Test
    public void testGetPersonNotFound() {
        Optional<Person> person = personService.getPerson("Alice", "Wonderland");
        assertFalse(person.isPresent());
    }

    @Test
    public void testGetChildrenByAddress() {

        List<ChildAlertDTO> children = personService.getChildrenByAddress("1509 Culver St");

        assertNotNull(children);
        assertEquals(1, children.size());
        ChildAlertDTO child = children.get(0);
        assertEquals("Jane", child.getFirstName());
        assertEquals("Doe", child.getLastName());

        int age = child.getAge();
        assertTrue(age <= 18);

        List<String> household = child.getHouseHoldMembers();
        assertNotNull(household);
        assertTrue(household.contains("John Doe"));
    }

    @Test
    public void testGetPersonInfoByLastName() {

        List<PersonInfoDTO> infos = personService.getPersonInfoByLastName("Doe");
        assertNotNull(infos);
        assertEquals(2, infos.size());

        PersonInfoDTO johnInfo = infos.stream()
                .filter(info -> info.getFirstName().equals("John"))
                .findFirst().orElse(null);
        assertNotNull(johnInfo);

        assertEquals("Doe", johnInfo.getLastName());
        assertEquals("1509 Culver St", johnInfo.getAddress());
        assertEquals("john.doe@example.com", johnInfo.getEmail());
        assertTrue(johnInfo.getAge() > 18);

        PersonInfoDTO janeInfo = infos.stream()
                .filter(info -> info.getFirstName().equals("Jane"))
                .findFirst().orElse(null);
        assertNotNull(janeInfo);

        assertEquals("Doe", janeInfo.getLastName());
        assertEquals("1509 Culver St", janeInfo.getAddress());
        assertEquals("jane.doe@example.com", janeInfo.getEmail());
        assertTrue(janeInfo.getAge() <= 18);
    }

    @Test
    public void testGetEmailsByCity() {

        List<String> emails = personService.getEmailsByCity("Culver");
        assertNotNull(emails);
        assertEquals(3, emails.size());
        assertTrue(emails.contains("john.doe@example.com"));
        assertTrue(emails.contains("jane.doe@example.com"));
        assertTrue(emails.contains("bob.smith@example.com"));
    }

    @Test
    public void testAddPersonSuccess() {
        Person newPerson = new Person();
        newPerson.setFirstName("Alice");
        newPerson.setLastName("Wonderland");
        newPerson.setAddress("123 Fantasy Rd");
        newPerson.setCity("Culver");
        newPerson.setEmail("alice@example.com");
        newPerson.setPhone("841-000-0000");
        newPerson.setZip("00000");

        Person added = personService.addPerson(newPerson);
        assertNotNull(added);
        Optional<Person> retrieved = personService.getPerson("Alice", "Wonderland");
        assertTrue(retrieved.isPresent());
        assertEquals("alice@example.com", retrieved.get().getEmail());
    }

    @Test
    public void testAddPersonAlreadyExists() {
        Person duplicate = new Person();
        duplicate.setFirstName("John");
        duplicate.setLastName("Doe");
        duplicate.setAddress("1509 Culver St");
        duplicate.setCity("Culver");
        duplicate.setEmail("john.duplicate@example.com");
        duplicate.setPhone("841-111-1111");
        duplicate.setZip("11111");

        Exception exception = assertThrows(BadRequestException.class, () -> {
            personService.addPerson(duplicate);
        });
        assertTrue(exception.getMessage().contains("This person already exists"));
    }

    @Test
    public void testUpdatePersonSuccess() {
        Person update = new Person();
        update.setFirstName("John");
        update.setLastName("Doe");
        update.setAddress("New Address 123");
        update.setCity("New City");
        update.setEmail("john.new@example.com");
        update.setPhone("841-222-2222");
        update.setZip("22222");

        boolean updated = personService.updatePerson(update);
        assertTrue(updated);
        Optional<Person> updatedPerson = personService.getPerson("John", "Doe");
        assertTrue(updatedPerson.isPresent());
        assertEquals("New Address 123", updatedPerson.get().getAddress());
        assertEquals("New City", updatedPerson.get().getCity());
        assertEquals("john.new@example.com", updatedPerson.get().getEmail());
    }

    @Test
    public void testDeletePersonSuccess() {
        boolean deleted = personService.deletePerson("Jane", "Doe");
        assertTrue(deleted);
        Optional<Person> person = personService.getPerson("Jane", "Doe");
        assertFalse(person.isPresent());
    }

    @Test
    public void testDeletePersonNotFound() {
        boolean deleted = personService.deletePerson("Non", "Existent");
        assertFalse(deleted);
    }
}
