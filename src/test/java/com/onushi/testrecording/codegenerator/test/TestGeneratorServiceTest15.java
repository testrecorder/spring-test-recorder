package com.onushi.testrecording.codegenerator.test;

import com.onushi.sample.model.Person;
import com.onushi.sample.services.PersonRepositoryImpl;
import com.onushi.sample.services.PersonService;
import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest15 extends TestGeneratorServiceTest {
    @Test
    void generateTestTargetWithDependencies() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = simpleDateFormat.parse("1940-11-27 00:00:00.000");
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new PersonService(new PersonRepositoryImpl()))
                .methodName("loadPerson")
                .arguments(Collections.singletonList(2))
                .result(Person.builder()
                        .dateOfBirth(date1)
                        .firstName("Bruce")
                        .lastName("Lee")
                        .build())
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class PersonServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void loadPerson() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "\n" +
                        "        PersonRepositoryImpl personRepositoryImpl1 = new PersonRepositoryImpl();\n" +
                        "        PersonService personService = new PersonService(personRepositoryImpl1);\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Person result = personService.loadPerson(2);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1940-11-27 00:00:00.000\"), result.getDateOfBirth());\n" +
                        "        assertEquals(\"Bruce\", result.getFirstName());\n" +
                        "        assertEquals(\"Lee\", result.getLastName());\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }
}
