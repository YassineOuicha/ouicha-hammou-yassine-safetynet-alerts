package com.safetynet.service;

import com.safetynet.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.safetynet.repository.DataRepository;

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

