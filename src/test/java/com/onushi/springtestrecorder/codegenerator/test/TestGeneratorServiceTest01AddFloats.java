package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest01AddFloats extends TestGeneratorServiceTest {
    @Test
    void generateTestForAddFloats() {
        // Arrange
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("addFloats")
                .arguments(Arrays.asList(2f, 3f))
                .build());
        assertEquals(testGenerator.getCurrentTestRecordingPhase(), TestRecordingPhase.DURING_METHOD_RUN);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(5f)
                .build());
        assertEquals(testGenerator.getCurrentTestRecordingPhase(), TestRecordingPhase.AFTER_METHOD_RUN);

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
                        "    void addFloats() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Float result = sampleService.addFloats(2.0f, 3.0f);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(5.0f, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }
}
