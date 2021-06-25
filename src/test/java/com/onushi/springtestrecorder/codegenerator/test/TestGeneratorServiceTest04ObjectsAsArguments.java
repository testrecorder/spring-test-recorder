/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.model.Person;
import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest04ObjectsAsArguments extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        Person person = Person.builder()
                .firstName("Mary")
                .lastName("Poe")
                .build();

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("getFirstName")
                .arguments(Collections.singletonList(person))
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result("Mary")
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
                        "import com.onushi.sample.model.Person;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void getFirstName() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        Person person1 = Person.builder()\n" +
                        "            .dateOfBirth(null)\n" +
                        "            .firstName(\"Mary\")\n" +
                        "            .lastName(\"Poe\")\n" +
                        "            .build();\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        String result = sampleService.getFirstName(person1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(\"Mary\", result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void getFirstName() {
        // Arrange
        Person person1 = Person.builder()
                .dateOfBirth(null)
                .firstName("Mary")
                .lastName("Poe")
                .build();
        SampleService sampleService = new SampleService();

        // Act
        String result = sampleService.getFirstName(person1);

        // Assert
        assertEquals("Mary", result);
    }
}
