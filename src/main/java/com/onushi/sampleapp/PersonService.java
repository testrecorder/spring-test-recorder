package com.onushi.sampleapp;

import com.onushi.testrecording.aspect.RecordTest;
import org.springframework.stereotype.Service;

// TODO IB !!!! Use PersonService and PersonRepository to show an example of Dependency Mocking
@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @RecordTest
    Person loadPerson(int id) throws Exception {
        return personRepository.getPerson(id);
    }
}
