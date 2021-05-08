package com.onushi.sampleapp;

import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    Person loadPerson(int id) throws Exception {
        return personRepository.getPerson(id);
    }
}
