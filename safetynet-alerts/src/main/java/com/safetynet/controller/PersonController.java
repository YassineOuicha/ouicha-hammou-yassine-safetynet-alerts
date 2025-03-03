package com.safetynet.controller;

import com.safetynet.model.ChildAlertDTO;
import com.safetynet.model.Person;
import com.safetynet.model.PersonInfoDTO;
import com.safetynet.service.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.safetynet.service.PersonService;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    /**
     * Endpoint to retrieve a list of all persons.
     *
     * @return a list of all persons
     */
    @GetMapping("/persons")
    public List<Person> getAllPersons(){
        return personService.getAllPersons();
    }

    /**
     * Endpoint to retrieve a person based on a combination of the first name and the last name.
     *
     * @param firstName the first name of the person to retrieve
     * @param lastName the last name of the person to retrieve
     * @return the person's détails, corresponding to the provided first and last name
     * @throws BadRequestException if the person is not found
     */
    @GetMapping ("/person")
    public Person getPerson(@RequestParam String firstName, @RequestParam String lastName){
        return personService.getPerson(firstName, lastName)
                .orElseThrow(()-> new BadRequestException("Person not found"));
    }

    /**
     * Endpoint to retrieve a list of children living at a specific address.
     *
     * @param address the address to check for children
     * @return a list containing children information for the given address
     */
    @GetMapping("/childAlert")
    public List<ChildAlertDTO> getChildrenByAddress(@RequestParam("address") String address){
        return personService.getChildrenByAddress(address);
    }

    /**
     * Endpoint to retrieve persons information by last name.
     *
     * @param lastName the last name of the persons to retrieve information for
     * @return a list containing person details for each person who has the same last name
     */
    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfoByLastName(@RequestParam String lastName){
        return personService.getPersonInfoByLastName(lastName);
    }

    /**
     * Endpoint to retrieve a list of community email addresses for a given city.
     *
     * @param city the city to get the community email addresses for
     * @return a list of email addresses for the specified city
     */
    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city){
        return personService.getEmailsByCity(city);
    }

    /**
     * Endpoint to add a new person.
     *
     * @param person the person data to be added
     * @return the added person information
     */
    @PostMapping("/person")
    public Person addPerson(@RequestBody Person person){
        return personService.addPerson(person);
    }

    /**
     * Endpoint to update an existing person.
     *
     * @param person the updated person data
     * @return true if the person was successfully updated, false otherwise
     */
    @PutMapping("/person")
    public boolean updatePerson(@RequestBody Person person){
        return personService.updatePerson(person);
    }

    /**
     * Endpoint to delete a person based on a combination of the first name and the last name.
     *
     * @param firstName the first name of the person to delete
     * @param lastName the last name of the person to delete
     * @return true if the person was successfully deleted, false otherwise
     */
    @DeleteMapping("/person")
    public boolean deletePerson(@RequestParam String firstName, @RequestParam String lastName){
        return personService.deletePerson(firstName, lastName);
    }

}
