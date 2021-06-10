package com.onushi.testrecording.codegenerator.test;

import com.onushi.sample.model.Department;
import com.onushi.sample.model.Employee;
import com.onushi.sample.services.SampleService;
import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest27  extends TestGeneratorServiceTest {
    // TODO IB !!!! activate @Test
    void testThatSideEffectsDoNotAffectTheArrangeCodeGeneration() throws Exception {
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

        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("modifyEmployee")
                .arguments(Collections.singletonList(employee))
                .result(null)
                .fallBackResultType(void.class)
                .dependencyMethodRuns(new ArrayList<>())
                .build();

        SampleService sampleService = new SampleService();
        sampleService.modifyEmployee(employee);

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

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
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));

    }
}