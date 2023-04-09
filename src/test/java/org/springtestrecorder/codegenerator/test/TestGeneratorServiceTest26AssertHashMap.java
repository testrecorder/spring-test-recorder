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
import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sample.model.Person;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest26AssertHashMap extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws Exception {
        // Arrange
        final Map<Person, Person> personMap = new SampleService().createPersonHashMap();

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createPersonHashMap")
                .arguments(Collections.emptyList())
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(personMap)
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
                "import java.util.Map;\n" +
                "import org.sample.model.Person;\n" +
                "import java.util.Date;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                "    //TODO rename the test to describe the use case\n" +
                "    //TODO refactor the generated code to make it easier to understand\n" +
                "    @Test\n" +
                "    void createPersonHashMap() throws Exception {\n" +
                "        // Arrange\n" +
                "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                "\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        Map<Person, Person> result = sampleService.createPersonHashMap();\n" +
                "\n" +
                "        // Assert\n" +
                "        Date date1 = simpleDateFormat.parse(\"1970-01-01 00:00:00.000\");\n" +
                "        Person person1 = Person.builder()\n" +
                "            .dateOfBirth(date1)\n" +
                "            .firstName(\"Marco\")\n" +
                "            .lastName(\"Polo\")\n" +
                "            .build();\n" +
                "\n" +
                "        Date date2 = simpleDateFormat.parse(\"1971-01-01 00:00:00.000\");\n" +
                "        Person person2 = Person.builder()\n" +
                "            .dateOfBirth(date2)\n" +
                "            .firstName(\"Tom\")\n" +
                "            .lastName(\"Richardson\")\n" +
                "            .build();\n" +
                "\n" +
                "        assertEquals(2, result.size());\n" +
                "\n" +
                "        assertEquals(simpleDateFormat.parse(\"1920-01-01 00:00:00.000\"), result.get(person1).getDateOfBirth());\n" +
                "        assertEquals(\"Marco'\", result.get(person1).getFirstName());\n" +
                "        assertEquals(\"Father\", result.get(person1).getLastName());\n" +
                "\n" +
                "        assertEquals(simpleDateFormat.parse(\"1921-01-01 00:00:00.000\"), result.get(person2).getDateOfBirth());\n" +
                "        assertEquals(\"Tom's\", result.get(person2).getFirstName());\n" +
                "        assertEquals(\"Father\", result.get(person2).getLastName());\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void createPersonHashMap() throws Exception {
        // Arrange
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        final SampleService sampleService = new SampleService();

        // Act
        final Map<Person, Person> result = sampleService.createPersonHashMap();

        // Assert
        final Date date1 = simpleDateFormat.parse("1970-01-01 00:00:00.000");
        final Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Marco")
                .lastName("Polo")
                .build();

        final Date date2 = simpleDateFormat.parse("1971-01-01 00:00:00.000");
        final Person person2 = Person.builder()
                .dateOfBirth(date2)
                .firstName("Tom")
                .lastName("Richardson")
                .build();

        assertEquals(2, result.size());

        assertEquals(simpleDateFormat.parse("1920-01-01 00:00:00.000"), result.get(person1).getDateOfBirth());
        assertEquals("Marco'", result.get(person1).getFirstName());
        assertEquals("Father", result.get(person1).getLastName());

        assertEquals(simpleDateFormat.parse("1921-01-01 00:00:00.000"), result.get(person2).getDateOfBirth());
        assertEquals("Tom's", result.get(person2).getFirstName());
        assertEquals("Father", result.get(person2).getLastName());
    }
}
