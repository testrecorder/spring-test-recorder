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

    @RecordTest
    public Person loadPerson(int id) throws Exception {
        return personRepository.getPersonFromDB(id);
    }
}
