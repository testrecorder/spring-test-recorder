package com.onushi.sampleapp.services;

import com.onushi.sampleapp.model.Person;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SampleServiceTest {
    //TODO rename the test to describe the use case
    //TODO refactor the generated code to make it easier to understand
    @Test
    void returnNull() throws Exception {
        // Arrange
        SampleService sampleService = new SampleService();

        // Act
        Person result = sampleService.returnNull();

        // Assert
        assertNull(result);
    }
}
