package com.onushi.testrecording.codegenerator.test;

import com.onushi.sample.services.SampleService;
import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest08 extends TestGeneratorServiceTest {
    @Test
    void generateTestForArrayLists() {
        // Arrange
        List<String> stringList = Arrays.asList("a", "b");
        List<Object> objectList = Arrays.asList(1, "b", null);
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processLists")
                .arguments(Arrays.asList(stringList, objectList))
                .result(42)
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
                        "import java.util.List;\n" +
                        "import java.util.Arrays;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processLists() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        List<String> arrayList1 = Arrays.asList(\"a\", \"b\");\n" +
                        "        List<Object> arrayList2 = Arrays.asList(1, \"b\", null);\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.processLists(arrayList1, arrayList2);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST =========\n"),
                StringUtils.prepareForCompare(testString));
    }
}
