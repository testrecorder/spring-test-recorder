/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.model.Person;
import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestGeneratorServiceTest21ListOfListOfPerson extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws Exception {
        // Arrange
        SampleService sampleService = new SampleService();
        List<List<Person>> listOfPersonList = sampleService.createListOfPersonList();

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createListOfPersonList")
                .arguments(Collections.emptyList())
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(listOfPersonList)
                .build());

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.List;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void createListOfPersonList() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        List<List<Person>> result = sampleService.createListOfPersonList();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(2, result.size());\n" +
                        "\n" +
                        "        assertEquals(2, result.get(0).size());\n" +
                        "\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1980-01-02 00:00:00.000\"), result.get(0).get(0).getDateOfBirth());\n" +
                        "        assertEquals(\"Paul\", result.get(0).get(0).getFirstName());\n" +
                        "        assertEquals(\"Thompson\", result.get(0).get(0).getLastName());\n" +
                        "\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1970-02-03 00:00:00.000\"), result.get(0).get(1).getDateOfBirth());\n" +
                        "        assertEquals(\"Tom\", result.get(0).get(1).getFirstName());\n" +
                        "        assertEquals(\"Richardson\", result.get(0).get(1).getLastName());\n" +
                        "\n" +
                        "        assertNull(result.get(1));\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void createListOfPersonList() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        SampleService sampleService = new SampleService();

        // Act
        List<List<Person>> result = sampleService.createListOfPersonList();

        // Assert
        assertEquals(2, result.size());

        assertEquals(2, result.get(0).size());

        assertEquals(simpleDateFormat.parse("1980-01-02 00:00:00.000"), result.get(0).get(0).getDateOfBirth());
        assertEquals("Paul", result.get(0).get(0).getFirstName());
        assertEquals("Thompson", result.get(0).get(0).getLastName());

        assertEquals(simpleDateFormat.parse("1970-02-03 00:00:00.000"), result.get(0).get(1).getDateOfBirth());
        assertEquals("Tom", result.get(0).get(1).getFirstName());
        assertEquals("Richardson", result.get(0).get(1).getLastName());

        assertNull(result.get(1));
    }
}
