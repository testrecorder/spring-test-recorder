package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.onushi.sampleapp.services.EmployeeRepository;

class SalaryServiceTest {
    @Test
    void computeEmployeeSalary() throws Exception {
        // Arrange
        EmployeeRepository employeeRepository1 = new EmployeeRepository();
        SalaryService salaryService = new SalaryService(employeeRepository1);

        // Act
        Double result = salaryService.computeEmployeeSalary(1);

        // Assert
        assertEquals(4000.0, result);
    }
}
