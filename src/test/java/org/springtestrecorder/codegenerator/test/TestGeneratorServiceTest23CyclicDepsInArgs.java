/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.sample.model.CyclicChild;
import org.sample.model.CyclicParent;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest23CyclicDepsInArgs extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws Exception {
        // Arrange
        final SampleService sampleService = new SampleService();
        final CyclicParent cyclicParent = sampleService.createCyclicObjects();

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(sampleService)
                .methodName("processCyclicObjects")
                .arguments(Collections.singletonList(cyclicParent))
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
                "import org.sample.model.CyclicParent;\n" +
                "import java.util.List;\n" +
                "import java.util.ArrayList;\n" +
                "import org.sample.model.CyclicChild;\n" +
                "import java.util.Arrays;\n" +
                "import java.util.Date;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                testGeneratorService.COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void processCyclicObjects() throws Exception {\n" +
                "        // Arrange\n" +
                "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                "\n" +
                "        Date date1 = simpleDateFormat.parse(\"1980-01-02 00:00:00.000\");\n" +
                "        // TODO Solve initialisation for cyclic dependency in org.sample.model.CyclicParent\n" +
                "        CyclicChild cyclicChild1 = new CyclicChild();\n" +
                "        cyclicChild1.date = date1;\n" +
                "        cyclicChild1.parent = ...;\n" +
                "        List<CyclicChild> singletonList1 = new ArrayList<>(Arrays.asList(cyclicChild1));\n" +
                "\n" +
                "        CyclicParent cyclicParent1 = new CyclicParent();\n" +
                "        cyclicParent1.childList = singletonList1;\n" +
                "        cyclicParent1.id = 1;\n" +
                "\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        Integer result = sampleService.processCyclicObjects(cyclicParent1);\n" +
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
    void processCyclicObjects() throws Exception {
        // Arrange
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        final Date date1 = simpleDateFormat.parse("1980-01-02 00:00:00.000");
        // TODO Solve initialisation for cyclic dependency in org.sample.model.CyclicParent
        final CyclicChild cyclicChild1 = new CyclicChild();
        cyclicChild1.date = date1;
        final List<CyclicChild> singletonList1 = new ArrayList<>(Collections.singletonList(cyclicChild1));

        final CyclicParent cyclicParent1 = new CyclicParent();
        cyclicParent1.childList = singletonList1;
        cyclicParent1.id = 1;
        cyclicChild1.parent = cyclicParent1;

        final SampleService sampleService = new SampleService();

        // Act
        final Integer result = sampleService.processCyclicObjects(cyclicParent1);

        // Assert
        assertEquals(42, result);
    }
}
