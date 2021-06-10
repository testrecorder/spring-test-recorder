package com.onushi.testrecording.codegenerator.test;

import com.onushi.sample.model.Person;
import com.onushi.sample.services.SampleService;
import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest25 extends TestGeneratorServiceTest {
    @Test
    void generateTestForHashSetAsserts() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth1 = simpleDateFormat.parse("1980-01-02");
        Date dateOfBirth2 = simpleDateFormat.parse("1970-02-03");
        Person marco = Person.builder()
                .firstName("Marco")
                .lastName("Polo")
                .dateOfBirth(dateOfBirth1)
                .build();

        Person tom = Person.builder()
                .firstName("Tom")
                .lastName("Richardson")
                .dateOfBirth(dateOfBirth2)
                .build();

        Set<Person> personSet = new HashSet<>();
        personSet.add(marco);
        personSet.add(tom);


        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createPersonHashSet")
                .arguments(Collections.emptyList())
                .result(personSet)
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
                        "import java.util.Set;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void createPersonHashSet() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Set<Person> result = sampleService.createPersonHashSet();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(2, result.size());\n" +
                        "\n" +
                        "        Date date1 = simpleDateFormat.parse(\"1980-01-02 00:00:00.000\");\n" +
                        "        Person person1 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"Marco\")\n" +
                        "            .lastName(\"Polo\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        assertTrue(result.contains(person1));\n" +
                        "\n" +
                        "        Date date2 = simpleDateFormat.parse(\"1970-02-03 00:00:00.000\");\n" +
                        "        Person person2 = Person.builder()\n" +
                        "            .dateOfBirth(date2)\n" +
                        "            .firstName(\"Tom\")\n" +
                        "            .lastName(\"Richardson\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        assertTrue(result.contains(person2));\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }
}
