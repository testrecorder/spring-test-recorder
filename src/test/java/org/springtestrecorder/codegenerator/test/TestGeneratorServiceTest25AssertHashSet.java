/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.sample.model.Person;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest25AssertHashSet extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws Exception {
        // Arrange
        final SampleService sampleService = new SampleService();
        final Set<Person> personSet = sampleService.createPersonHashSet();

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createPersonHashSet")
                .arguments(Collections.emptyList())
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(personSet)
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
                "import java.util.Set;\n" +
                "import org.sample.model.Person;\n" +
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
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        final SampleService sampleService = new SampleService();

        // Act
        final Set<Person> result = sampleService.createPersonHashSet();

        // Assert
        assertEquals(2, result.size());

        final Date date1 = simpleDateFormat.parse("1980-01-02 00:00:00.000");
        final Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Marco")
                .lastName("Polo")
                .build();

        assertTrue(result.contains(person1));

        final Date date2 = simpleDateFormat.parse("1970-02-03 00:00:00.000");
        final Person person2 = Person.builder()
                .dateOfBirth(date2)
                .firstName("Tom")
                .lastName("Richardson")
                .build();

        assertTrue(result.contains(person2));
    }
}
