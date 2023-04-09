/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sample.model.Person;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest04ObjectsAsArguments extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final Person person = Person.builder()
                .firstName("Mary")
                .lastName("Poe")
                .build();

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("getFirstName")
                .arguments(Collections.singletonList(person))
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result("Mary")
                .build());
        // Act
        final String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        Assertions.assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                "\n" +
                "package org.sample.services;\n" +
                "\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import static org.junit.jupiter.api.Assertions.*;\n" +
                "import org.sample.model.Person;\n" +
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
        final Person person1 = Person.builder()
                .dateOfBirth(null)
                .firstName("Mary")
                .lastName("Poe")
                .build();
        final SampleService sampleService = new SampleService();

        // Act
        final String result = sampleService.getFirstName(person1);

        // Assert
        assertEquals("Mary", result);
    }
}
