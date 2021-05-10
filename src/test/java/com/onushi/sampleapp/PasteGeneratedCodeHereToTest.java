
package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.onushi.sampleapp.services.PersonRepositoryImpl;

import java.util.NoSuchElementException;

class PersonServiceTest {
    @Test
    void getPersonFirstName() throws Exception {
        // Arrange
        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);
        when(personRepositoryImpl1.getPersonsCountFromDB("a", null)).thenReturn(2);
        when(personRepositoryImpl1.getPersonFromDB(2)).thenThrow(new NoSuchElementException());
        PersonService personService = new PersonService(personRepositoryImpl1);

        // Act
        Object result = personService.getPersonFirstName(3);

        // Assert
        assertEquals(null, result);
    }
}
