package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.services.PersonRepositoryImpl;
import com.onushi.sample.services.PersonService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class TestGeneratorServiceTest18ExceptionMock extends TestGeneratorServiceTest {
    @Test
    void generateTestWithExceptionThrownByMockCall() {
        // Arrange
        PersonRepositoryImpl personRepositoryImpl = new PersonRepositoryImpl();
        DependencyMethodRunInfo dependencyMethodRunInfo1 = DependencyMethodRunInfo.builder()
                .threadId(Thread.currentThread().getId())
                .target(personRepositoryImpl)
                .methodName("getPersonsCountFromDB")
                .arguments(Arrays.asList("a", null))
                .result(2)
                .build();
        DependencyMethodRunInfo dependencyMethodRunInfo2 = DependencyMethodRunInfo.builder()
                .threadId(Thread.currentThread().getId())
                .target(personRepositoryImpl)
                .methodName("getPersonFromDB")
                .arguments(Collections.singletonList(3))
                .result(null)
                .exception(new NoSuchElementException())
                .build();

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
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
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
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
        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);
        when(personRepositoryImpl1.getPersonsCountFromDB("a", null)).thenReturn(2);
        doThrow(NoSuchElementException.class)
                .when(personRepositoryImpl1).getPersonFromDB(3);
        PersonService personService = new PersonService(personRepositoryImpl1);

        // Act
        Object result = personService.getPersonFirstName(3);

        // Assert
        assertNull(result);
    }
}
