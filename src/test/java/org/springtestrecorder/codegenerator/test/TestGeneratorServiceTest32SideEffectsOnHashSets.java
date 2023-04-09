/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.sample.model.Person;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest32SideEffectsOnHashSets extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws ParseException {
        // Arrange
        final SampleService sampleService = new SampleService();
        final Set<Person> hashSet = sampleService.createPersonHashSet();

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(sampleService)
                .methodName("changeHashSet")
                .arguments(Collections.singletonList(hashSet))
                .build());
        sampleService.changeHashSet(hashSet);
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
                "import java.util.Set;\n" +
                "import org.sample.model.Person;\n" +
                "import java.util.HashSet;\n" +
                "import java.util.Date;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                "    //TODO rename the test to describe the use case\n" +
                "    //TODO refactor the generated code to make it easier to understand\n" +
                "    @Test\n" +
                "    void changeHashSet() throws Exception {\n" +
                "        // Arrange\n" +
                "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                "\n" +
                "        Date date1 = simpleDateFormat.parse(\"1980-01-02 00:00:00.000\");\n" +
                "        Person person1 = Person.builder()\n" +
                "            .dateOfBirth(date1)\n" +
                "            .firstName(\"Marco\")\n" +
                "            .lastName(\"Polo\")\n" +
                "            .build();\n" +
                "\n" +
                "        Date date2 = simpleDateFormat.parse(\"1970-02-03 00:00:00.000\");\n" +
                "        Person person2 = Person.builder()\n" +
                "            .dateOfBirth(date2)\n" +
                "            .firstName(\"Tom\")\n" +
                "            .lastName(\"Richardson\")\n" +
                "            .build();\n" +
                "\n" +
                "        Set<Person> hashSet1 = new HashSet<>();\n" +
                "        hashSet1.add(person1);\n" +
                "        hashSet1.add(person2);\n" +
                "\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        Integer result = sampleService.changeHashSet(hashSet1);\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(42, result);\n" +
                "\n" +
                "        // Side Effects\n" +
                "        assertEquals(3, hashSet1.size());\n" +
                "\n" +
                "        Date date3 = simpleDateFormat.parse(\"1980-01-03 00:00:00.000\");\n" +
                "        Person person3 = Person.builder()\n" +
                "            .dateOfBirth(date3)\n" +
                "            .firstName(\"FN\")\n" +
                "            .lastName(\"LN\")\n" +
                "            .build();\n" +
                "\n" +
                "        assertTrue(hashSet1.contains(person3));\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void changeHashSet() throws Exception {
        // Arrange
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        final Date date1 = simpleDateFormat.parse("1980-01-02 00:00:00.000");
        final Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Marco")
                .lastName("Polo")
                .build();

        final Date date2 = simpleDateFormat.parse("1970-02-03 00:00:00.000");
        final Person person2 = Person.builder()
                .dateOfBirth(date2)
                .firstName("Tom")
                .lastName("Richardson")
                .build();

        final Set<Person> hashSet1 = new HashSet<>();
        hashSet1.add(person1);
        hashSet1.add(person2);

        final SampleService sampleService = new SampleService();

        // Act
        final Integer result = sampleService.changeHashSet(hashSet1);

        // Assert
        assertEquals(42, result);

        // Side Effects
        assertEquals(3, hashSet1.size());

        final Date date3 = simpleDateFormat.parse("1980-01-03 00:00:00.000");
        final Person person3 = Person.builder()
                .dateOfBirth(date3)
                .firstName("FN")
                .lastName("LN")
                .build();

        assertTrue(hashSet1.contains(person3));
    }
}
