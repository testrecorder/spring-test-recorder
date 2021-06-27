/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import org.springtestrecorder.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest29SideEffectsOnDates extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws ParseException {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = simpleDateFormat.parse("2021-01-01 00:00:00.000");

        SampleService sampleService = new SampleService();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(sampleService)
                .methodName("changeDate")
                .arguments(Collections.singletonList(date))
                .build());
        sampleService.changeDate(date);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(42)
                .build());

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package org.springtestrecorder.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    //TODO rename the test to describe the use case\n" +
                        "    //TODO refactor the generated code to make it easier to understand\n" +
                        "    @Test\n" +
                        "    void changeDate() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "\n" +
                        "        Date date1 = simpleDateFormat.parse(\"2021-01-01 00:00:00.000\");\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.changeDate(date1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "\n" +
                        "        // Side Effects\n" +
                        "        assertEquals(simpleDateFormat.parse(\"2021-01-01 01:00:00.000\"), date1);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void changeDate() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date date1 = simpleDateFormat.parse("2021-01-01 00:00:00.000");
        SampleService sampleService = new SampleService();

        // Act
        Integer result = sampleService.changeDate(date1);

        // Assert
        assertEquals(42, result);

        // Side Effects
        assertEquals(simpleDateFormat.parse("2021-01-01 01:00:00.000"), date1);
    }
}
