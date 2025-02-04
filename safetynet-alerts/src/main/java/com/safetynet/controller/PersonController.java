package com.safetynet.controller;

import com.safetynet.model.ChildAlertDTO;
import com.safetynet.model.Person;
import com.safetynet.model.PersonInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.safetynet.service.PersonService;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/persons")
    public List<Person> getAllPersons(){
        return personService.getAllPersons();
    }

    @GetMapping ("/person")
    public Person getPerson(@RequestParam String firstName, @RequestParam String lastName){
        return personService.getPerson(firstName, lastName)
                .orElseThrow(()-> new RuntimeException("Person not found"));
    }
    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildrenByAddress(@RequestParam("address") String address){
        return personService.getChildrenByAddress(address);
    }

    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfoByLastName(@RequestParam String lastName){
        return personService.getPersonInfoByLastName(lastName);
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city){
        return personService.getEmailsByCity(city);
    }

    @PostMapping("/person")
    public Person addPerson(@RequestBody Person person){
        return personService.addPerson(person);
    }

    @PutMapping("/person")
    public boolean updatePerson(@RequestBody Person person){
        return personService.updatePerson(person);
    }

    @DeleteMapping("/person")
    public boolean deletePerson(@RequestParam String firstName, @RequestParam String lastName){
        return personService.deletePerson(firstName, lastName);
    }



}
