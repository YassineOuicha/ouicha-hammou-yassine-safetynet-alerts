package com.safetynet.service;

import com.safetynet.model.ChildAlertDTO;
import com.safetynet.model.MedicalRecord;
import com.safetynet.model.Person;
import com.safetynet.model.PersonInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.safetynet.repository.DataRepository;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final DataService dataService;

    @Autowired
    public PersonService(DataService dataService) {
        this.dataService = dataService;
    }

    public List<Person> getAllPersons(){
        DataRepository dataRepository = dataService.getData();
        return dataRepository.getPersons();
    }

    public Optional<Person> getPerson(String firstName, String lastName){

        return getAllPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }

    public List<ChildAlertDTO> getChildrenByAddress(String address) {
        ChildAlertDTO dto = new ChildAlertDTO();
        List<Person> personsSameArea = getAllPersons().stream().filter(p->p.getAddress().equalsIgnoreCase(address)).toList();

        List<ChildAlertDTO> childrenList = new ArrayList<>();

        for(Person p: personsSameArea){
            MedicalRecord medicalRecord = dataService.getData().getMedicalrecords().stream()
                    .filter(mr-> mr.getFirstName().equalsIgnoreCase(p.getFirstName())
                            && mr.getLastName().equalsIgnoreCase(p.getLastName()))
                    .findFirst()
                    .orElse(null);

            if (medicalRecord != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
                int age = calculateAge(birthDate);

                if (age <= 18) {
                    ChildAlertDTO childAlertDTO = new ChildAlertDTO();
                    childAlertDTO.setFirstName(p.getFirstName());
                    childAlertDTO.setLastName(p.getLastName());
                    childAlertDTO.setAge(age);

                    List<String> houseHoldMembers = personsSameArea.stream()
                            .filter(p2-> !p2.getFirstName().equalsIgnoreCase(p.getFirstName())
                                                 && p2.getLastName().equalsIgnoreCase(p.getLastName()))
                            .map(p2 -> p2.getFirstName() + " " + p2.getLastName())
                            .toList();
                    childAlertDTO.setHouseHoldMembers(houseHoldMembers);
                    childrenList.add(childAlertDTO);
                }
            }
        }
        return childrenList;
    }

    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName){

        List<PersonInfoDTO> relatives = new ArrayList<>();

        List<Person> personsWithSameLastNames = getAllPersons().stream()
                .filter(p-> p.getLastName().equalsIgnoreCase(lastName))
                .toList();

        for(Person person:personsWithSameLastNames){
            MedicalRecord medicalRecord = dataService.getData().getMedicalrecords().stream()
                    .filter(mr-> mr.getFirstName().equalsIgnoreCase(person.getFirstName())
                    && mr.getLastName().equalsIgnoreCase(person.getLastName()))
                    .findFirst()
                    .orElse(null);
            if(medicalRecord != null){
                PersonInfoDTO personInfoDTO = new PersonInfoDTO();
                personInfoDTO.setFirstName(person.getFirstName());
                personInfoDTO.setLastName(person.getLastName());
                personInfoDTO.setAddress(person.getAddress());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), formatter);
                int age = calculateAge(birthDate);
                personInfoDTO.setAge(age);

                personInfoDTO.setEmail(person.getEmail());
                personInfoDTO.setMedications(medicalRecord.getMedications());
                personInfoDTO.setAllergies(medicalRecord.getAllergies());

                relatives.add(personInfoDTO);
            }
        }
        return relatives;
    }

    public List<String> getEmailsByCity(String city) {
        return getAllPersons().stream()
                .filter(p-> p.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct()
                .toList();
    }
    private int calculateAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        if(birthDate!=null){
            return Period.between(birthDate, currentDate).getYears();
        }
        return 0;
    }

    public Person addPerson(Person person){
        Optional<Person> existingPerson = getPerson(person.getFirstName(), person.getLastName());
        if(existingPerson.isPresent()){
            throw new RuntimeException("This person already exists");
        }
        dataService.getData().getPersons().add(person);
        return person;
    }

    public boolean updatePerson (Person updatedPerson){
        Person outDatedPerson = getPerson(updatedPerson.getFirstName(), updatedPerson.getLastName())
                .orElseThrow(() -> new RuntimeException("Person not found"));
        outDatedPerson.setAddress(updatedPerson.getAddress());
        outDatedPerson.setCity(updatedPerson.getCity());
        outDatedPerson.setPhone(updatedPerson.getPhone());
        outDatedPerson.setZip(updatedPerson.getZip());
        outDatedPerson.setEmail(updatedPerson.getEmail());

        return true;
    }

    public boolean deletePerson (String firstName, String lastName){

        return  getAllPersons().removeIf(p-> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName));
    }

}

