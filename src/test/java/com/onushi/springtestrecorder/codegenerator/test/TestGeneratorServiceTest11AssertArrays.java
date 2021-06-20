package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest11AssertArrays extends TestGeneratorServiceTest {
    @Test
    void generateTestForMethodThatReturnsArray() {
        // Arrange
        int[] result = {3, 4};

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("returnIntArray")
                .arguments(Collections.emptyList())
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(result)
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
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void returnIntArray() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        int[] result = sampleService.returnIntArray();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(2, result.length);\n" +
                        "        assertEquals(3, result[0]);\n" +
                        "        assertEquals(4, result[1]);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void returnIntArray() throws Exception {
        // Arrange
        SampleService sampleService = new SampleService();

        // Act
        int[] result = sampleService.returnIntArray();

        // Assert
        assertEquals(2, result.length);
        assertEquals(3, result[0]);
        assertEquals(4, result[1]);
    }
}
