/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.sample.model.CyclicParent;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest24CyclicDepsInResult extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws Exception {
        // Arrange
        final SampleService sampleService = new SampleService();
        final CyclicParent cyclicParent = sampleService.createCyclicObjects();

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(sampleService)
                .methodName("createCyclicObjects")
                .arguments(Collections.emptyList())
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(cyclicParent)
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
                "import org.sample.model.CyclicParent;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                testGeneratorService.COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void createCyclicObjects() throws Exception {\n" +
                "        // Arrange\n" +
                "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                "\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        CyclicParent result = sampleService.createCyclicObjects();\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(1, result.childList.size());\n" +
                "        assertEquals(simpleDateFormat.parse(\"1980-01-02 00:00:00.000\"), result.childList.get(0).date);\n" +
                "        assertEquals(1, result.id);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void createCyclicObjects() throws Exception {
        // Arrange
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        final SampleService sampleService = new SampleService();

        // Act
        final CyclicParent result = sampleService.createCyclicObjects();

        // Assert
        assertEquals(1, result.childList.size());
        assertEquals(simpleDateFormat.parse("1980-01-02 00:00:00.000"), result.childList.get(0).date);
        assertEquals(1, result.id);
    }
}
