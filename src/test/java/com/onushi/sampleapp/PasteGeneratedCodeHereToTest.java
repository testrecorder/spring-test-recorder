
        package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.HashSet;

class SampleServiceTest {
    @Test
    void processSet() throws Exception {
        // Arrange
        Set<Double> hashSet1 = new HashSet<>();
        hashSet1.add(null);
        hashSet1.add(1.2);
        hashSet1.add(2.6);
        SampleService sampleService = new SampleService();

        // Act
        Float result = sampleService.processSet(hashSet1);

        // Assert
        assertEquals(42.42f, result);
    }
}

