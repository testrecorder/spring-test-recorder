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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest08ForArrayLists extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final List<String> stringList = Arrays.asList("a", "b");
        final List<Object> objectList = Arrays.asList(1, "b", null);

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processLists")
                .arguments(Arrays.asList(stringList, objectList))
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(42)
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
                "import java.util.Arrays;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                testGeneratorService.COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void processLists() throws Exception {\n" +
                "        // Arrange\n" +
                "        List<String> arrayList1 = new ArrayList<>(Arrays.asList(\"a\", \"b\"));\n" +
                "        List<Object> arrayList2 = new ArrayList<>(Arrays.asList(1, \"b\", null));\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        Integer result = sampleService.processLists(arrayList1, arrayList2);\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(42, result);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST =========\n"),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void processLists() {
        // Arrange
        final List<String> arrayList1 = new ArrayList<>(Arrays.asList("a", "b"));
        final List<Object> arrayList2 = new ArrayList<>(Arrays.asList(1, "b", null));
        final SampleService sampleService = new SampleService();

        // Act
        final Integer result = sampleService.processLists(arrayList1, arrayList2);

        // Assert
        assertEquals(42, result);
    }
}
