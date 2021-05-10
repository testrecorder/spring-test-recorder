package com.onushi.sampleapp.services;

import com.onushi.sampleapp.model.Person;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.onushi.sampleapp.services.PersonRepositoryImpl;

class PersonServiceTest {
    @Test
    void loadPerson() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        PersonRepositoryImpl personRepositoryImpl1 = new PersonRepositoryImpl();
        PersonService personService = new PersonService(personRepositoryImpl1);

        // Act
        Person result = personService.loadPerson(2);

        // Assert
        Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        Person expectedResult = Person.builder()
                .dateOfBirth(date1)
                .firstName("Bruce")
                .lastName("Lee")
                .build();
        assertEquals(expectedResult, result);
    }
}

