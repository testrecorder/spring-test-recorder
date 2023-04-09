/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sample.model.StudentWithBuilder;
import org.sample.model.StudentWithDefaultInitFields;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest14NoArgsConstructor extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final StudentWithDefaultInitFields student1 = new StudentWithDefaultInitFields();
        final StudentWithBuilder student2 = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Wayne")
                .age(60)
                .build();

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processStudents")
                .arguments(Arrays.asList(student1, student2))
                .fallBackResultType(void.class)
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .exception(null)
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
                "import org.sample.model.StudentWithDefaultInitFields;\n" +
                "import org.sample.model.StudentWithBuilder;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                testGeneratorService.COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void processStudents() throws Exception {\n" +
                "        // Arrange\n" +
                "        StudentWithDefaultInitFields studentWithDefaultInitFields1 = new StudentWithDefaultInitFields();\n" +
                "\n" +
                "        StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()\n" +
                "            .age(60)\n" +
                "            .firstName(\"John\")\n" +
                "            .lastName(\"Wayne\")\n" +
                "            .build();\n" +
                "\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        sampleService.processStudents(studentWithDefaultInitFields1, studentWithBuilder1);\n" +
                "\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void processStudents() {
        // Arrange
        final StudentWithDefaultInitFields studentWithDefaultInitFields1 = new StudentWithDefaultInitFields();

        final StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()
                .age(60)
                .firstName("John")
                .lastName("Wayne")
                .build();

        final SampleService sampleService = new SampleService();

        // Act
        sampleService.processStudents(studentWithDefaultInitFields1, studentWithBuilder1);

    }
}
