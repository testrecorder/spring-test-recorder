package com.onushi.sampleapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void getArrayLength() throws Exception {
        // Arrange
        Object[] array1 = {"1", "2"};
        SampleService sampleService = new SampleService();

        // Act
        int result = sampleService.getArrayLength(array1);

        // Assert
        assertEquals(2, result);
    }


}
