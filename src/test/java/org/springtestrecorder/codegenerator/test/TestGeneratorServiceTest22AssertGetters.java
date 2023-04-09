/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.sample.model.Color;
import org.sample.model.Employee;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest22AssertGetters extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final SampleService sampleService = new SampleService();
        final Employee employee = sampleService.createEmployee();

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(sampleService)
                .methodName("createEmployee")
                .arguments(Collections.emptyList())
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(employee)
                .build());

        // Act
        final String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                "\n" +
                "package org.sample.services;\n" +
                "\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import static org.junit.jupiter.api.Assertions.*;\n" +
                "import org.sample.model.Employee;\n" +
                "import org.sample.model.Color;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                "    //TODO rename the test to describe the use case\n" +
                "    //TODO refactor the generated code to make it easier to understand\n" +
                "    @Test\n" +
                "    void createEmployee() throws Exception {\n" +
                "        // Arrange\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        Employee result = sampleService.createEmployee();\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(100, result.getDepartment().getId());\n" +
                "        assertEquals(\"IT\", result.getDepartment().getName());\n" +
                "\n" +
                "        assertEquals(\"John\", result.getFirstName());\n" +
                "\n" +
                "        assertEquals(1, result.getId());\n" +
                "\n" +
                "        assertEquals(\"Doe\", result.getLastName());\n" +
                "\n" +
                "        assertEquals(1000.0, result.getSalaryParam1());\n" +
                "\n" +
                "        assertEquals(1500.0, result.getSalaryParam2());\n" +
                "\n" +
                "        assertEquals(0.0, result.getSalaryParam3());\n" +
                "\n" +
                "        assertEquals(Color.BLUE, result.getTeamColor());\n" +
                "\n" +
                "        assertFalse(result.isTeamLeader());\n" +
                "\n" +
                "        assertFalse(result.isTeamLeader);\n" +
                "\n" +
                "        assertEquals(Color.BLUE, result.teamColor);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void createEmployee() {
        // Arrange
        final SampleService sampleService = new SampleService();

        // Act
        final Employee result = sampleService.createEmployee();

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

        assertFalse(result.isTeamLeader());

        assertFalse(result.isTeamLeader);

        assertEquals(Color.BLUE, result.teamColor);
    }
}
