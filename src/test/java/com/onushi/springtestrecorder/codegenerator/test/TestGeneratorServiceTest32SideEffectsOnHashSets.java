package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.model.Person;
import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGeneratorServiceTest32SideEffectsOnHashSets extends TestGeneratorServiceTest {
    @Test
    void generateTest() throws ParseException {
        // Arrange
        SampleService sampleService = new SampleService();
        Set<Person> hashSet = sampleService.createPersonHashSet();

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(sampleService)
                .methodName("changeHashSet")
                .arguments(Collections.singletonList(hashSet))
                .build());
        sampleService.changeHashSet(hashSet);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(42)
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
                        "import java.util.Set;\n" +
                        "import com.onushi.sample.model.Person;\n" +
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date date1 = simpleDateFormat.parse("1980-01-02 00:00:00.000");
        Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Marco")
                .lastName("Polo")
                .build();

        Date date2 = simpleDateFormat.parse("1970-02-03 00:00:00.000");
        Person person2 = Person.builder()
                .dateOfBirth(date2)
                .firstName("Tom")
                .lastName("Richardson")
                .build();

        Set<Person> hashSet1 = new HashSet<>();
        hashSet1.add(person1);
        hashSet1.add(person2);

        SampleService sampleService = new SampleService();

        // Act
        Integer result = sampleService.changeHashSet(hashSet1);

        // Assert
        assertEquals(42, result);

        // Side Effects
        assertEquals(3, hashSet1.size());

        Date date3 = simpleDateFormat.parse("1980-01-03 00:00:00.000");
        Person person3 = Person.builder()
                .dateOfBirth(date3)
                .firstName("FN")
                .lastName("LN")
                .build();

        assertTrue(hashSet1.contains(person3));
    }
}
