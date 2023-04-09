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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sample.model.Person;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest09DependenciesOfDependencies extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws Exception {
        // Arrange
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date dateOfBirth1 = simpleDateFormat.parse("1980-01-02");
        final Date dateOfBirth2 = simpleDateFormat.parse("1970-02-03");
        final Person paul = Person.builder()
                .firstName("Paul")
                .lastName("Thompson")
                .dateOfBirth(dateOfBirth1)
                .build();

        final Person tom = Person.builder()
                .firstName("Tom")
                .lastName("Richardson")
                .dateOfBirth(dateOfBirth2)
                .build();

        final List<Person> personList = Arrays.asList(paul, tom);
        final Person[] personArray = { paul, tom };

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("someFunction")
                .arguments(Arrays.asList(personList, personArray))
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(personList)
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
                "import org.sample.model.Person;\n" +
                "import java.util.Arrays;\n" +
                "import java.util.Date;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                testGeneratorService.COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void someFunction() throws Exception {\n" +
                "        // Arrange\n" +
                "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                "\n" +
                "        Date date1 = simpleDateFormat.parse(\"1980-01-02 00:00:00.000\");\n" +
                "        Person person1 = Person.builder()\n" +
                "            .dateOfBirth(date1)\n" +
                "            .firstName(\"Paul\")\n" +
                "            .lastName(\"Thompson\")\n" +
                "            .build();\n" +
                "\n" +
                "        Date date2 = simpleDateFormat.parse(\"1970-02-03 00:00:00.000\");\n" +
                "        Person person2 = Person.builder()\n" +
                "            .dateOfBirth(date2)\n" +
                "            .firstName(\"Tom\")\n" +
                "            .lastName(\"Richardson\")\n" +
                "            .build();\n" +
                "\n" +
                "        List<Person> arrayList1 = new ArrayList<>(Arrays.asList(person1, person2));\n" +
                "\n" +
                "        Person[] array1 = {person1, person2};\n" +
                "\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        List<Person> result = sampleService.someFunction(arrayList1, array1);\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(2, result.size());\n" +
                "        assertEquals(person1, result.get(0));\n" +
                "        assertEquals(person2, result.get(1));\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void someFunction() throws Exception {
        // Arrange
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        final Date date1 = simpleDateFormat.parse("1980-01-02 00:00:00.000");
        final Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Paul")
                .lastName("Thompson")
                .build();

        final Date date2 = simpleDateFormat.parse("1970-02-03 00:00:00.000");
        final Person person2 = Person.builder()
                .dateOfBirth(date2)
                .firstName("Tom")
                .lastName("Richardson")
                .build();

        final List<Person> arrayList1 = new ArrayList<>(Arrays.asList(person1, person2));

        final Person[] array1 = { person1, person2 };

        final SampleService sampleService = new SampleService();

        // Act
        final List<Person> result = sampleService.someFunction(arrayList1, array1);

        // Assert
        assertEquals(2, result.size());
        assertEquals(person1, result.get(0));
        assertEquals(person2, result.get(1));
    }
}
