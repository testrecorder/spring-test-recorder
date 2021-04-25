package com.onushi.sampleapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.onushi.sampleapp.Person;

class SampleServiceTest {
    @Test
    void getFirstName() throws Exception {
        // Arrange
        Person person1 = Person.builder()
                .dateOfBirth(null)
                .firstName("Mary")
                .lastName("Poe")
                .build();
        SampleService sampleService = new SampleService();

        // Act
        String result = sampleService.getFirstName(person1);

        // Assert
        assertEquals("Mary", result);
    }
}
