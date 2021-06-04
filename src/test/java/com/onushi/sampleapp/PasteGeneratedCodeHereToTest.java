package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.onushi.sampleapp.model.Color;
import com.onushi.sampleapp.model.Employee;

class SampleServiceTest {
    //TODO rename the test to describe the use case
    //TODO refactor the generated code to make it easier to understand
    @Test
    void getEmployee() throws Exception {
        // Arrange
        SampleService sampleService = new SampleService();

        // Act
        Employee result = sampleService.getEmployee();

        // Assert
        assertEquals(100, result.getDepartment().getId());
        assertEquals("IT", result.getDepartment().getName());
        assertEquals("John", result.getFirstName());
        assertEquals(1, result.getId());
        assertEquals("Doe", result.getLastName());
        assertEquals(1000.0, result.getSalaryParam1());
        assertEquals(1500.0, result.getSalaryParam2());
        assertEquals(0.0, result.getSalaryParam3());
        assertEquals(Color.BLUE, result.getTeamColor());
        assertEquals(Color.BLUE, result.teamColor);
    }
}
