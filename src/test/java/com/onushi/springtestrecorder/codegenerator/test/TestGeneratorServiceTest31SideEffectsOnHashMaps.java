package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.model.Person;
import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest31SideEffectsOnHashMaps extends TestGeneratorServiceTest {
    @Test
    void generateTestWithSideEffectsOnHashMaps() throws ParseException {
        // Arrange
        SampleService sampleService = new SampleService();
        Map<Person, Person> personsHashMap = sampleService.createPersonHashMap();

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(sampleService)
                .methodName("changeHashMap")
                .arguments(Collections.singletonList(personsHashMap))
                .build());
        sampleService.changeHashMap(personsHashMap);
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
                        "import java.util.Map;\n" +
                        "import com.onushi.sample.model.Person;\n" +
                        "import java.util.HashMap;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    //TODO rename the test to describe the use case\n" +
                        "    //TODO refactor the generated code to make it easier to understand\n" +
                        "    @Test\n" +
                        "    void changeHashMap() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "\n" +
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
                        "        Date date2 = simpleDateFormat.parse(\"1920-02-04 00:00:00.000\");\n" +
                        "        Person person3 = Person.builder()\n" +
                        "            .dateOfBirth(date2)\n" +
                        "            .firstName(\"Tom's\")\n" +
                        "            .lastName(\"Father\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        Date date3 = simpleDateFormat.parse(\"1920-02-03 00:00:00.000\");\n" +
                        "        Person person4 = Person.builder()\n" +
                        "            .dateOfBirth(date3)\n" +
                        "            .firstName(\"Marco'\")\n" +
                        "            .lastName(\"Father\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        Map<Person, Person> hashMap1 = new HashMap<>();\n" +
                        "        hashMap1.put(person1, person4);\n" +
                        "        hashMap1.put(person2, person3);\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.changeHashMap(hashMap1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "\n" +
                        "        // Side Effects\n" +
                        "        Person person5 = Person.builder()\n" +
                        "            .dateOfBirth(date1)\n" +
                        "            .firstName(\"FN\")\n" +
                        "            .lastName(\"LN\")\n" +
                        "            .build();\n" +
                        "\n" +
                        "        assertEquals(1, hashMap1.size());\n" +
                        "\n" +
                        "        assertEquals(date1, hashMap1.get(person5).getDateOfBirth());\n" +
                        "        assertEquals(\"FN1'\", hashMap1.get(person5).getFirstName());\n" +
                        "        assertEquals(\"LN1\", hashMap1.get(person5).getLastName());\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void changeHashMap() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date date1 = simpleDateFormat.parse("1970-01-02 00:00:00.000");
        Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Marco")
                .lastName("Polo")
                .build();

        Person person2 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Tom")
                .lastName("Richardson")
                .build();

        Date date2 = simpleDateFormat.parse("1920-02-04 00:00:00.000");
        Person person3 = Person.builder()
                .dateOfBirth(date2)
                .firstName("Tom's")
                .lastName("Father")
                .build();

        Date date3 = simpleDateFormat.parse("1920-02-03 00:00:00.000");
        Person person4 = Person.builder()
                .dateOfBirth(date3)
                .firstName("Marco'")
                .lastName("Father")
                .build();

        Map<Person, Person> hashMap1 = new HashMap<>();
        hashMap1.put(person1, person4);
        hashMap1.put(person2, person3);

        SampleService sampleService = new SampleService();

        // Act
        Integer result = sampleService.changeHashMap(hashMap1);

        // Assert
        assertEquals(42, result);

        // Side Effects
        Person person5 = Person.builder()
                .dateOfBirth(date1)
                .firstName("FN")
                .lastName("LN")
                .build();

        assertEquals(1, hashMap1.size());

        assertEquals(date1, hashMap1.get(person5).getDateOfBirth());
        assertEquals("FN1'", hashMap1.get(person5).getFirstName());
        assertEquals("LN1", hashMap1.get(person5).getLastName());
    }
}
