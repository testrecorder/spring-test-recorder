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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest20HashSet extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        Set<Double> set = new HashSet<>();
        set.add(null);
        set.add(1.2);
        set.add(2.6);

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processSet")
                .arguments(Collections.singletonList(set))
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(42.42f)
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
                        "import java.util.Set;\n" +
                        "import java.util.HashSet;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processSet() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        Set<Double> hashSet1 = new HashSet<>();\n" +
                        "        hashSet1.add(null);\n" +
                        "        hashSet1.add(1.2);\n" +
                        "        hashSet1.add(2.6);\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Float result = sampleService.processSet(hashSet1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42.42f, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void processSet() {
        // Arrange
        Set<Double> hashSet1 = new HashSet<>();
        hashSet1.add(null);
        hashSet1.add(1.2);
        hashSet1.add(2.6);
        SampleService sampleService = new SampleService();

        // Act
        Float result = sampleService.processSet(hashSet1);

        // Assert
        assertEquals(42.42f, result);
    }
}
