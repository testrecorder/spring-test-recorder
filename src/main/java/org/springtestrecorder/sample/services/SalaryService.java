/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.sample.services;

import org.springtestrecorder.sample.model.Employee;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {
    private final EmployeeRepository employeeRepository;

    public SalaryService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public double computeEmployeeSalary(int employeeId) {
        Employee employee = employeeRepository.loadEmployee(employeeId);
        return employee.getSalaryParam1() + 2 * employee.getSalaryParam2();
    }
}
