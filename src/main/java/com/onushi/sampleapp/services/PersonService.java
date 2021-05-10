package com.onushi.sampleapp.services;

import com.onushi.sampleapp.model.Person;
import com.onushi.testrecording.aspect.RecordTest;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person loadPerson(int id) throws Exception {
        return personRepository.getPersonFromDB(id);
    }

    @RecordTest
    public String getPersonFirstName(int id) throws Exception {
        personRepository.getPersonsCountFromDB("a", null);
        try {
            Person person = personRepository.getPersonFromDB(id);
            return person.getFirstName();
        } catch (Exception ex) {
            return null;
        }
    }
}
