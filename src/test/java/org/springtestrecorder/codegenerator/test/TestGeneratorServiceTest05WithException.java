/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest05WithException extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("testException")
                .arguments(Collections.singletonList(5))
                .fallBackResultType(String.class)
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .exception(new IllegalArgumentException("x"))
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
                "\n" +
                "class SampleServiceTest {\n" +
                testGeneratorService.COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void testException() throws Exception {\n" +
                "        // Arrange\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act & Assert\n" +
                "        assertThrows(java.lang.IllegalArgumentException.class, () -> sampleService.testException(5));\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void testException() {
        // Arrange
        final SampleService sampleService = new SampleService();

        // Act & Assert
        assertThrows(java.lang.IllegalArgumentException.class, () -> sampleService.testException(5));
    }
}
