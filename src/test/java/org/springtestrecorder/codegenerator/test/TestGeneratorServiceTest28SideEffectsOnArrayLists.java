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
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest28SideEffectsOnArrayLists extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final List<Float> floatList = new ArrayList<>();
        floatList.add(0f);
        floatList.add(2f);
        floatList.add(3f);

        final SampleService sampleService = new SampleService();
        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(sampleService)
                .methodName("changeArrayList")
                .arguments(Collections.singletonList(floatList))
                .build());
        sampleService.changeArrayList(floatList);
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
                "    //TODO rename the test to describe the use case\n" +
                "    //TODO refactor the generated code to make it easier to understand\n" +
                "    @Test\n" +
                "    void changeArrayList() throws Exception {\n" +
                "        // Arrange\n" +
                "        List<Float> arrayList1 = new ArrayList<>(Arrays.asList(0.0f, 2.0f, 3.0f));\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        Integer result = sampleService.changeArrayList(arrayList1);\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(42, result);\n" +
                "\n" +
                "        // Side Effects\n" +
                "        assertEquals(4, arrayList1.size());\n" +
                "        assertEquals(1.0f, arrayList1.get(1));\n" +
                "        assertEquals(2.0f, arrayList1.get(2));\n" +
                "        assertEquals(3.0f, arrayList1.get(3));\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void changeArrayList() {
        // Arrange
        final List<Float> arrayList1 = new ArrayList<>(Arrays.asList(0.0f, 2.0f, 3.0f));
        final SampleService sampleService = new SampleService();

        // Act
        final Integer result = sampleService.changeArrayList(arrayList1);

        // Assert
        assertEquals(42, result);

        // Side Effects
        assertEquals(4, arrayList1.size());
        assertEquals(1.0f, arrayList1.get(1));
        assertEquals(2.0f, arrayList1.get(2));
        assertEquals(3.0f, arrayList1.get(3));
    }
}
