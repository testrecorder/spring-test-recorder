package com.onushi.testrecorder.codegenerator.test;

import com.onushi.sample.model.Person;
import com.onushi.sample.services.SampleService;
import com.onushi.testrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.testrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.testrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest26AssertHashMap extends TestGeneratorServiceTest {
    @Test
    void generateAssertTestForHashMap() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth1 = simpleDateFormat.parse("1970-01-02");
        Date dateOfBirth2 = simpleDateFormat.parse("1970-01-02");
        Date dateOfBirth3 = simpleDateFormat.parse("1920-02-03");
        Date dateOfBirth4 = simpleDateFormat.parse("1920-02-04");
        Person marco = Person.builder()
                .firstName("Marco")
                .lastName("Polo")
                .dateOfBirth(dateOfBirth1)
                .build();

        Person marcosFather = Person.builder()
                .firstName("Marco'")
                .lastName("Father")
                .dateOfBirth(dateOfBirth3)
                .build();

        Person tom = Person.builder()
                .firstName("Tom")
                .lastName("Richardson")
                .dateOfBirth(dateOfBirth2)
                .build();

        Person tomsFather = Person.builder()
                .firstName("Tom's")
                .lastName("Father")
                .dateOfBirth(dateOfBirth4)
                .build();

        Map<Person, Person> personMap = new HashMap<>();
        personMap.put(marco, marcosFather);
        personMap.put(tom, tomsFather);

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createPersonHashMap")
                .arguments(Collections.emptyList())
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(personMap)
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
                        "import java.util.Map;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
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
                        "        Date date1 = simpleDateFormat.parse(\"1970-01-02 00:00:00.000\");\n" +
                        "        Person person1 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Marco\")\n" +
                        "            .lastName(\"Polo\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        Person person2 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Tom\")\n" +
                        "            .lastName(\"Richardson\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        assertEquals(2, result.size());\n" +
                        "\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1920-02-03 00:00:00.000\"), result.get(person1).getDateOfBirth());\n" +
                        "        assertEquals(\"Marco'\", result.get(person1).getFirstName());\n" +
                        "        assertEquals(\"Father\", result.get(person1).getLastName());\n" +
                        "\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1920-02-04 00:00:00.000\"), result.get(person2).getDateOfBirth());\n" +
                        "        assertEquals(\"Tom's\", result.get(person2).getFirstName());\n" +
                        "        assertEquals(\"Father\", result.get(person2).getLastName());\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }
}
