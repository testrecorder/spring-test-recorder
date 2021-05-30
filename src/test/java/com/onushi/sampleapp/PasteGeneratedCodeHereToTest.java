package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SampleServiceTest {
    //TODO rename the test to describe the use case
    //TODO refactor the generated code to make it easier to understand
    @Test
    void getList() throws Exception {
        // Arrange
        SampleService sampleService = new SampleService();

        // Act
        List<Integer> result = sampleService.getList();

        // Assert
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
    }
}
