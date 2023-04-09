/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.sample.services.PersonRepositoryImpl;
import org.sample.services.PersonService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest18ExceptionMock extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final PersonRepositoryImpl personRepositoryImpl = new PersonRepositoryImpl();
        final DependencyMethodRunInfo dependencyMethodRunInfo1 = DependencyMethodRunInfo.builder()
                .threadId(Thread.currentThread().getId())
                .target(personRepositoryImpl)
                .methodName("getPersonsCountFromDB")
                .arguments(Arrays.asList("a", null))
                .result(2)
                .build();
        final DependencyMethodRunInfo dependencyMethodRunInfo2 = DependencyMethodRunInfo.builder()
                .threadId(Thread.currentThread().getId())
                .target(personRepositoryImpl)
                .methodName("getPersonFromDB")
                .arguments(Collections.singletonList(3))
                .result(null)
                .exception(new NoSuchElementException())
                .build();

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new PersonService(personRepositoryImpl))
                .methodName("getPersonFirstName")
                .arguments(Collections.singletonList(3))
                .build());
        testGeneratorFactory.addDependencyMethodRun(testGenerator, dependencyMethodRunInfo1);
        testGeneratorFactory.addDependencyMethodRun(testGenerator, dependencyMethodRunInfo2);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
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
                "\n" +
                "class PersonServiceTest {\n" +
                testGeneratorService.COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void getPersonFirstName() throws Exception {\n" +
                "        // Arrange\n" +
                "        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);\n" +
                "        when(personRepositoryImpl1.getPersonsCountFromDB(\"a\", null)).thenReturn(2);\n" +
                "        doThrow(NoSuchElementException.class)\n" +
                "            .when(personRepositoryImpl1).getPersonFromDB(3);\n" +
                "        PersonService personService = new PersonService(personRepositoryImpl1);\n" +
                "\n" +
                "        // Act\n" +
                "        Object result = personService.getPersonFirstName(3);\n" +
                "\n" +
                "        // Assert\n" +
                "        assertNull(result);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void getPersonFirstName() throws Exception {
        // Arrange
        final PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);
        when(personRepositoryImpl1.getPersonsCountFromDB("a", null)).thenReturn(2);
        doThrow(NoSuchElementException.class)
                .when(personRepositoryImpl1).getPersonFromDB(3);
        final PersonService personService = new PersonService(personRepositoryImpl1);

        // Act
        final Object result = personService.getPersonFirstName(3);

        // Assert
        assertNull(result);
    }
}
