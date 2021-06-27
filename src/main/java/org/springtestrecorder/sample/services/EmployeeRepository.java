/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.sample.services;

import org.springtestrecorder.sample.model.Department;
import org.springtestrecorder.sample.model.Employee;
import org.springtestrecorder.aspect.RecordMockForTest;
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
