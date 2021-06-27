/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.sample.services;

import org.springtestrecorder.sample.model.Person;
import org.springtestrecorder.aspect.RecordTest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<Integer, Person> loadPersons(List<Integer> personIds) throws Exception {
        Map<Integer, Person> result = new HashMap<>();
        if (personIds != null) {
            for (Integer personId : personIds) {
                Person person = personRepository.getPersonFromDB(personId);
                result.put(personId, person);
            }

        }
        return result;
    }

    public String getPersonFirstName(int id) {
        try {
            Person person = personRepository.getPersonFromDB(id);
            return person.getFirstName();
        } catch (Exception ex) {
            return null;
        }
    }
}
