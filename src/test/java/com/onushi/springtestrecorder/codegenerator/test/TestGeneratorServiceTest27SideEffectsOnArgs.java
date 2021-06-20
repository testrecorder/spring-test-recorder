package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.model.Department;
import com.onushi.sample.model.Employee;
import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest27SideEffectsOnArgs extends TestGeneratorServiceTest {
    @Test
    void testThatSideEffectsDoNotAffectTheArrangeCodeGeneration() {
        Employee employee = Employee.builder()
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

        SampleService sampleService = new SampleService();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(sampleService)
                .methodName("modifyEmployee")
                .arguments(Collections.singletonList(employee))
                .fallBackResultType(void.class)
                .build());

        sampleService.modifyEmployee(employee);

        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sample.model.Employee;\n" +
                        "import com.onushi.sample.model.Department;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    //TODO rename the test to describe the use case\n" +
                        "    //TODO refactor the generated code to make it easier to understand\n" +
                        "    @Test\n" +
                        "    void modifyEmployee() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        Department department1 = Department.builder()\n" +
                        "            .id(100)\n" +
                        "            .name(\"IT\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        Employee employee1 = Employee.builder()\n" +
                        "            .department(department1)\n" +
                        "            .firstName(\"John\")\n" +
                        "            .id(1)\n" +
                        "            .isTeamLeader(false)\n" +
                        "            .lastName(\"Doe\")\n" +
                        "            .salaryParam1(1000.0)\n" +
                        "            .salaryParam2(1500.0)\n" +
                        "            .salaryParam3(0.0)\n" +
                        "            .teamColor(null)\n" +
                        "            .build();\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        sampleService.modifyEmployee(employee1);\n" +
                        "\n" +
                        "\n" +
                        "        // Side Effects\n" +
                        "        assertEquals(2000.0, employee1.getSalaryParam1());\n" +
                        "        assertEquals(4500.0, employee1.getSalaryParam2());\n" +
                        // TODO IB !!!! "        assertEquals(\"IT New\", employee1.getDepartment().getName());\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));

    }

    @Test
    void modifyEmployee() {
        // Arrange
        Department department1 = Department.builder()
                .id(100)
                .name("IT")
                .build();

        Employee employee1 = Employee.builder()
                .department(department1)
                .firstName("John")
                .id(1)
                .isTeamLeader(false)
                .lastName("Doe")
                .salaryParam1(1000.0)
                .salaryParam2(1500.0)
                .salaryParam3(0.0)
                .teamColor(null)
                .build();

        SampleService sampleService = new SampleService();

        // Act
        sampleService.modifyEmployee(employee1);


        // Side Effects
        assertEquals(2000.0, employee1.getSalaryParam1());
        assertEquals(4500.0, employee1.getSalaryParam2());
    }
}
