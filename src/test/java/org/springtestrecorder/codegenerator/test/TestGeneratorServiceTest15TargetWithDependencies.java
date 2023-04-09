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
import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.sample.model.Person;
import org.sample.services.PersonRepositoryImpl;
import org.sample.services.PersonService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest15TargetWithDependencies extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws Exception {
        // Arrange
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        final Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new PersonService(new PersonRepositoryImpl()))
                .methodName("loadPerson")
                .arguments(Collections.singletonList(2))
                .build());

        final Person person = Person.builder()
                .dateOfBirth(date1)
                .firstName("Bruce")
                .lastName("Lee")
                .build();

        final PersonRepositoryImpl personRepositoryImpl = new PersonRepositoryImpl();
        final DependencyMethodRunInfo dependencyMethodRunInfo = DependencyMethodRunInfo.builder()
                .threadId(Thread.currentThread().getId())
                .target(personRepositoryImpl)
                .methodName("getPersonFromDB")
                .arguments(Collections.singletonList(2))
                .result(person)
                .build();
        testGeneratorFactory.addDependencyMethodRun(testGenerator, dependencyMethodRunInfo);

        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(person)
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
                "import static org.mockito.Mockito.*;\n" +
                "import org.sample.model.Person;\n" +
                "import java.util.Date;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "\n" +
                "class PersonServiceTest {\n" +
                "    //TODO rename the test to describe the use case\n" +
                "    //TODO refactor the generated code to make it easier to understand\n" +
                "    @Test\n" +
                "    void loadPerson() throws Exception {\n" +
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
                "        when(personRepositoryImpl1.getPersonFromDB(2)).thenReturn(person1);\n" +
                "\n" +
                "        PersonService personService = new PersonService(personRepositoryImpl1);\n" +
                "\n" +
                "        // Act\n" +
                "        Person result = personService.loadPerson(2);\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(person1, result);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void loadPerson() throws Exception {
        // Arrange
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        final Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        final Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Bruce")
                .lastName("Lee")
                .build();

        final PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);
        when(personRepositoryImpl1.getPersonFromDB(2)).thenReturn(person1);

        final PersonService personService = new PersonService(personRepositoryImpl1);

        // Act
        final Person result = personService.loadPerson(2);

        // Assert
        assertEquals(person1, result);
    }
}
