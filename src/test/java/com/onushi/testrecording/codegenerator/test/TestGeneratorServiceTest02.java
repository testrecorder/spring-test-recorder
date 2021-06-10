package com.onushi.testrecording.codegenerator.test;

import com.onushi.sample.services.SampleService;
import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.utils.ServiceCreatorUtils;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest02 {
    TestGeneratorFactory testGeneratorFactory;
    TestGeneratorService testGeneratorService;

    @BeforeEach
    void setUp() {
        testGeneratorFactory = ServiceCreatorUtils.createTestGeneratorFactory();
        testGeneratorService = ServiceCreatorUtils.createTestGeneratorService();
    }

    @Test
    void generateTestForMinDate() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = simpleDateFormat.parse("2021-01-01");
        Date d2 = simpleDateFormat.parse("2021-02-02");
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("minDate")
                .arguments(Arrays.asList(d1, d2))
                .result(d1)
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
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void minDate() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "\n" +
                        "        Date date1 = simpleDateFormat.parse(\"2021-01-01 00:00:00.000\");\n" +
                        "        Date date2 = simpleDateFormat.parse(\"2021-02-02 00:00:00.000\");\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Date result = sampleService.minDate(date1, date2);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(date1, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }
}
