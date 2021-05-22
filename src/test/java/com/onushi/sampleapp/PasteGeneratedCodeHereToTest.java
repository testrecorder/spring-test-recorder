package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.onushi.sampleapp.model.Color;

class SampleServiceTest {
    @Test
    void testEnum() throws Exception {
        // Arrange
        SampleService sampleService = new SampleService();

        // Act
        int result = sampleService.testEnum(Color.BLUE);

        // Assert
        assertEquals(2, result);
    }
}
