/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest13RepeatedArgs extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final int[] intArray = { 3, 4, 3 };
        final List<Float> floatList = Arrays.asList(3.0f, 3.0f);

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("repeatedArgs")
                .arguments(Arrays.asList(intArray, floatList))
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(42)
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
                "import java.util.List;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.Arrays;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                testGeneratorService.COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void repeatedArgs() throws Exception {\n" +
                "        // Arrange\n" +
                "        int[] array1 = {3, 4, 3};\n" +
                "        List<Float> arrayList1 = new ArrayList<>(Arrays.asList(3.0f, 3.0f));\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        Integer result = sampleService.repeatedArgs(array1, arrayList1);\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(42, result);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void repeatedArgs() {
        // Arrange
        final int[] array1 = { 3, 4, 3 };
        final List<Float> arrayList1 = new ArrayList<>(Arrays.asList(3.0f, 3.0f));
        final SampleService sampleService = new SampleService();

        // Act
        final Integer result = sampleService.repeatedArgs(array1, arrayList1);

        // Assert
        assertEquals(42, result);
    }
}
