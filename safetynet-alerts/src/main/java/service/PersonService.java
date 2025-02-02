package service;

import model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DataRepository;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private DataService dataService;

    public List<Person> getAllPersons(){
        DataRepository dataRepository = dataService.getData();

        return dataService.getData().getPersons();
    }



}

