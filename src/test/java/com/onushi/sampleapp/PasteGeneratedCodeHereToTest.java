package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.onushi.sampleapp.model.Person;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.onushi.sampleapp.services.PersonRepositoryImpl;

class PersonServiceTest {
    @Test
    void getPersonFirstName() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Bruce")
                .lastName("Lee")
                .build();
        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);
        when(personRepositoryImpl1.getPersonsCountFromDB("first", "second")).thenReturn(2);
        when(personRepositoryImpl1.getPersonFromDB(2)).thenReturn(person1);
        PersonService personService = new PersonService(personRepositoryImpl1);

        // Act
        String result = personService.getPersonFirstName(2);

        // Assert
        assertEquals("Bruce", result);
    }
}
