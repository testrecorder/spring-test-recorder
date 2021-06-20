package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.model.Person;
import com.onushi.sample.services.PersonRepositoryImpl;
import com.onushi.sample.services.PersonService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestGeneratorServiceTest17Mock2Calls extends TestGeneratorServiceTest {
    @Test
    void generateTestWith2MockCalls() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        PersonRepositoryImpl personRepositoryImpl = new PersonRepositoryImpl();
        DependencyMethodRunInfo dependencyMethodRunInfo1 = DependencyMethodRunInfo.builder()
                .threadId(Thread.currentThread().getId())
                .target(personRepositoryImpl)
                .methodName("getPersonsCountFromDB")
                .arguments(Arrays.asList("a", null))
                .result(2)
                .build();
        Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        DependencyMethodRunInfo dependencyMethodRunInfo2 = DependencyMethodRunInfo.builder()
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

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
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
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import static org.mockito.Mockito.*;\n" +
                        "import com.onushi.sample.model.Person;\n" +
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Bruce")
                .lastName("Lee")
                .build();

        PersonRepositoryImpl personRepositoryImpl1 = mock(PersonRepositoryImpl.class);
        when(personRepositoryImpl1.getPersonsCountFromDB("a", null)).thenReturn(2);
        when(personRepositoryImpl1.getPersonFromDB(2)).thenReturn(person1);

        PersonService personService = new PersonService(personRepositoryImpl1);

        // Act
        String result = personService.getPersonFirstName(2);

        // Assert
        assertEquals("Bruce", result);
    }
}
