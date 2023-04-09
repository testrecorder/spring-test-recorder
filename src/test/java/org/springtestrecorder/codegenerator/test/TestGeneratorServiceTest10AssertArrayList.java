/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest10AssertArrayList extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createList")
                .arguments(Collections.emptyList())
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(Arrays.asList(1, 2, 3))
                .build());

        // Act
        final String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        Assertions.assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                "\n" +
                "package org.sample.services;\n" +
                "\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import static org.junit.jupiter.api.Assertions.*;\n" +
                "import java.util.List;\n" +
                "import java.util.ArrayList;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                testGeneratorService.COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void createList() throws Exception {\n" +
                "        // Arrange\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        List<Integer> result = sampleService.createList();\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(3, result.size());\n" +
                "        assertEquals(1, result.get(0));\n" +
                "        assertEquals(2, result.get(1));\n" +
                "        assertEquals(3, result.get(2));\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void createList() {
        // Arrange
        final SampleService sampleService = new SampleService();

        // Act
        final List<Integer> result = sampleService.createList();

        // Assert
        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
    }
}
