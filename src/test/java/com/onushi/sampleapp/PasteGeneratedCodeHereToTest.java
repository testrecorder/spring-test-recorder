package com.onushi.sampleapp.services;

import com.onushi.sampleapp.model.Person;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.onushi.sampleapp.services.PersonRepositoryImpl;

import java.text.SimpleDateFormat;

class PersonServiceTest {
    @Test
    void getPersonFirstName() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Person person1 = Person.builder()
                .firstName("Bruce")
                .lastName("Lee")
                .dateOfBirth(simpleDateFormat.parse("1940-11-27"))
                .build();
        PersonRepositoryImpl personRepositoryImpl = mock(PersonRepositoryImpl.class);
        when(personRepositoryImpl.getPersonFromDB(any(int.class))).thenReturn(person1);

        PersonService personService = new PersonService(personRepositoryImpl);

        // Act
        String result = personService.getPersonFirstName(2);

        // Assert
        assertEquals("Bruce", result);
    }
}
