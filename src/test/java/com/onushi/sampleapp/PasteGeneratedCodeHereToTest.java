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
        PersonRepositoryImpl personRepositoryImpl = mock(PersonRepositoryImpl.class);
        when(personRepositoryImpl.getPersonsCountFromDB("a", null)).thenReturn(2);
        when(personRepositoryImpl.getPersonFromDB(3)).thenThrow(NoSuchElementException.class);
        PersonService personService = new PersonService(personRepositoryImpl);

        // Act
        String result = personService.getPersonFirstName(3);

        // Assert
        assertEquals(null, result);
    }
}
