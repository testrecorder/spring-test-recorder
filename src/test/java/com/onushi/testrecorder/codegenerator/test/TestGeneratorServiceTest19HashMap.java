package com.onushi.testrecorder.codegenerator.test;

import com.onushi.sample.services.SampleService;
import com.onushi.testrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.testrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.testrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest19HashMap extends TestGeneratorServiceTest {
    @Test
    void generateTestForHashMaps() {
        // Arrange
        Map<String, List<String>> map = new HashMap<>();
        map.put("1", Arrays.asList("0", "1"));
        map.put("2", Arrays.asList("0", "1", "2"));
        map.put("3", Arrays.asList("0", "1", "2", "3"));

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processMap")
                .arguments(Collections.singletonList(map))
                .build());
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
                        "import java.util.List;\n" +
                        "import java.util.HashMap;\n" +
                        "import java.util.Arrays;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processMap() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        List<String> arrayList1 = Arrays.asList(\"0\", \"1\");\n" +
                        "\n" +
                        "        List<String> arrayList2 = Arrays.asList(\"0\", \"1\", \"2\");\n" +
                        "\n" +
                        "        List<String> arrayList3 = Arrays.asList(\"0\", \"1\", \"2\", \"3\");\n" +
                        "\n" +
                        "        Map<String, List<String>> hashMap1 = new HashMap<>();\n" +
                        "        hashMap1.put(\"1\", arrayList1);\n" +
                        "        hashMap1.put(\"2\", arrayList2);\n" +
                        "        hashMap1.put(\"3\", arrayList3);\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.processMap(hashMap1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }
}
