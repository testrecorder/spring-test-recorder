package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.onushi.sampleapp.model.Department;
import com.onushi.sampleapp.model.Employee;

class SampleServiceTest {
    //Test Generated on 2021-05-29 09:43:34.698 with @RecordTest
    //TODO rename the test to describe the use case
    //TODO refactor the generated code to make it easier to understand
    @Test
    void modifyEmployee() throws Exception {
        // Arrange
        Department department1 = Department.builder()
                .id(100)
                .name("IT")
                .build();
        Employee employee1 = Employee.builder()
                .department(department1)
                .firstName("John")
                .id(1)
                .lastName("Doe")
                .salaryParam1(1000)
                .salaryParam2(1500)
                .salaryParam3(0.0)
                .build();
        SampleService sampleService = new SampleService();

        // Act
        sampleService.modifyEmployee(employee1);

        // Assert
        // TODO IB !!!! !!!! this is how the assert should look like
        assertEquals(1100.000023841858, employee1.getSalaryParam1());
        assertEquals(1650.0000357627869, employee1.getSalaryParam2());
        assertEquals(0.0, employee1.getSalaryParam3());
        assertEquals("IT New", department1.getName());
    }
}
