package com.onushi.testrecording.codegenerator.test;

import com.onushi.sample.services.PersonRepository;
import com.onushi.sample.services.SampleService;
import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.utils.ServiceCreatorUtils;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest3 {
    TestGeneratorFactory testGeneratorFactory;
    TestGeneratorService testGeneratorService;

    @BeforeEach
    void setUp() {
        testGeneratorFactory = ServiceCreatorUtils.createTestGeneratorFactory();
        testGeneratorService = ServiceCreatorUtils.createTestGeneratorService();
    }

    @Test
    void generateTestForReturnNull() {
        // Arrange
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("returnNull")
                .arguments(Collections.emptyList())
                .result(null)
                .fallBackResultType(PersonRepository.class)
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
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void returnNull() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        PersonRepository result = sampleService.returnNull();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertNull(result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));

    }
}
