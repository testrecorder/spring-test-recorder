/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sample.model.Person;
import org.sample.services.PersonRepositoryImpl;
import org.sample.services.PersonService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest17Mock2Calls extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws Exception {
        // Arrange
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        final PersonRepositoryImpl personRepositoryImpl = new PersonRepositoryImpl();
        final DependencyMethodRunInfo dependencyMethodRunInfo1 = DependencyMethodRunInfo.builder()
                .threadId(Thread.currentThread().getId())
                .target(personRepositoryImpl)
                .methodName("getPersonsCountFromDB")
                .arguments(Arrays.asList("a", null))
                .result(2)
                .build();
        final Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        final DependencyMethodRunInfo dependencyMethodRunInfo2 = DependencyMethodRunInfo.builder()
                .threadId(Thread.currentThread().getId())
                .target(personRepositoryImpl)
                .methodName("getPersonFromDB")
                .arguments(Collections.singletonList(2))
                .result(Person.builder()
                        .dateOfBirth(date1)
                        .firstName("Bruce")
                        .lastName("Lee")
                        .build())
                .build();

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new PersonService(personRepositoryImpl))
                .methodName("getPersonFirstName")
                .arguments(Collections.singletonList(2))
                .build());
        testGeneratorFactory.addDependencyMethodRun(testGenerator, dependencyMethodRunInfo1);
        testGeneratorFactory.addDependencyMethodRun(testGenerator, dependencyMethodRunInfo2);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result("Bruce")
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
                "import static org.mockito.Mockito.*;\n" +
                "import org.sample.model.Person;\n" +
                "import java.util.Date;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "\n" +
                "class PersonServiceTest {\n" +
                testGeneratorService.COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void getPersonFirstName() throws Exception {\n" +
                "        // Arrange\n" +
                "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                "\n" +
                "        Date date1 = simpleDateFormat.parse(\"1940-11-27 00:00:00.000\");\n" +
                "        Person person1 = Person.builder()\n" +
                "            .dateOfBirth(date1)\n" +
                "            .firstName(\"Bruce\")\n" +
                "            .lastName(\"Lee\")\n" +
                "            .build();\n" +
                "\n" +
                "        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);\n" +
                "        when(personRepositoryImpl1.getPersonsCountFromDB(\"a\", null)).thenReturn(2);\n" +
                "        when(personRepositoryImpl1.getPersonFromDB(2)).thenReturn(person1);\n" +
                "\n" +
                "        PersonService personService = new PersonService(personRepositoryImpl1);\n" +
                "\n" +
                "        // Act\n" +
                "        String result = personService.getPersonFirstName(2);\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(\"Bruce\", result);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void getPersonFirstName() throws Exception {
        // Arrange
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        final Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        final Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Bruce")
                .lastName("Lee")
                .build();

        final PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);
        when(personRepositoryImpl1.getPersonsCountFromDB("a", null)).thenReturn(2);
        when(personRepositoryImpl1.getPersonFromDB(2)).thenReturn(person1);

        final PersonService personService = new PersonService(personRepositoryImpl1);

        // Act
        final String result = personService.getPersonFirstName(2);

        // Assert
        assertEquals("Bruce", result);
    }
}
