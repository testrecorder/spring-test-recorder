package com.onushi.testrecorder.codegenerator.test;

import com.onushi.sample.model.Color;
import com.onushi.sample.model.Department;
import com.onushi.sample.model.Employee;
import com.onushi.sample.services.SampleService;
import com.onushi.testrecorder.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest22 extends TestGeneratorServiceTest {
    @Test
    void generateTestForObjectsWithGetters() {
        // Arrange
        Employee employee = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .salaryParam1(1000)
                .salaryParam2(1500)
                .teamColor(Color.BLUE)
                .department(Department.builder()
                        .id(100)
                        .name("IT")
                        .build())
                .build();

        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createEmployee")
                .arguments(Collections.emptyList())
                .result(employee)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sample.model.Employee;\n" +
                        "import com.onushi.sample.model.Color;\n" +
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
}
