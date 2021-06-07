package com.onushi.sampleapp.services;

import com.onushi.sampleapp.model.Department;
import com.onushi.sampleapp.model.Employee;
import com.onushi.testrecording.aspect.RecordMockForTest;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@RecordMockForTest
@Repository
public class EmployeeRepository {
    public Employee loadEmployee(int id) {
        if (id == 1) {
            return Employee.builder()
                    .id(1)
                    .firstName("John")
                    .lastName("Doe")
                    .salaryParam1(1000)
                    .salaryParam2(1500)
                    .department(Department.builder()
                            .id(100)
                            .name("IT")
                            .build())
                    .build();

        } else {
            throw new NoSuchElementException();
        }
    }
}
