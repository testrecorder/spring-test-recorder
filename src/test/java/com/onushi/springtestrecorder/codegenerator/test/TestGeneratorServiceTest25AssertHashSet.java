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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGeneratorServiceTest25AssertHashSet extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws Exception {
        // Arrange
        SampleService sampleService = new SampleService();
        Set<Person> personSet = sampleService.createPersonHashSet();

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createPersonHashSet")
                .arguments(Collections.emptyList())
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(personSet)
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
                        "import java.util.Set;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void createPersonHashSet() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Set<Person> result = sampleService.createPersonHashSet();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(2, result.size());\n" +
                        "\n" +
                        "        Date date1 = simpleDateFormat.parse(\"1980-01-02 00:00:00.000\");\n" +
                        "        Person person1 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Marco\")\n" +
                        "            .lastName(\"Polo\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        assertTrue(result.contains(person1));\n" +
                        "\n" +
                        "        Date date2 = simpleDateFormat.parse(\"1970-02-03 00:00:00.000\");\n" +
                        "        Person person2 = Person.builder()\n" +
                        "            .dateOfBirth(date2)\n" +
                        "            .firstName(\"Tom\")\n" +
                        "            .lastName(\"Richardson\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        assertTrue(result.contains(person2));\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void createPersonHashSet() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        SampleService sampleService = new SampleService();

        // Act
        Set<Person> result = sampleService.createPersonHashSet();

        // Assert
        assertEquals(2, result.size());

        Date date1 = simpleDateFormat.parse("1980-01-02 00:00:00.000");
        Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Marco")
                .lastName("Polo")
                .build();

        assertTrue(result.contains(person1));

        Date date2 = simpleDateFormat.parse("1970-02-03 00:00:00.000");
        Person person2 = Person.builder()
                .dateOfBirth(date2)
                .firstName("Tom")
                .lastName("Richardson")
                .build();

        assertTrue(result.contains(person2));
    }
}
