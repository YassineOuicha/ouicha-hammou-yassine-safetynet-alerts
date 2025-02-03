package com.safetynet.controller;

import com.safetynet.model.Person;
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

    @GetMapping
    public Person getPerson(@RequestParam String firstName, @RequestParam String lastName){
        return personService.getPerson(firstName, lastName)
                .orElseThrow(()-> new RuntimeException("Person not found"));
    }

    @PostMapping("/add")
    public Person addPerson(@RequestBody Person person){
        return personService.addPerson(person);
    }

    @PutMapping("/update")
    public boolean updatePerson(@RequestBody Person person){
        return personService.updatePerson(person);
    }

    @DeleteMapping("/delete")
    public boolean deletePerson(@RequestParam String firstName, @RequestParam String lastName){
        return personService.deletePerson(firstName, lastName);
    }




}
