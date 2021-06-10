package com.onushi.testrecording.codegenerator.test;

import com.onushi.sample.model.CyclicChild;
import com.onushi.sample.model.CyclicParent;
import com.onushi.sample.services.SampleService;
import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest24 extends TestGeneratorServiceTest {
    @Test
    void generateTestForCyclicDependenciesInResult() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CyclicParent cyclicParent = new CyclicParent();
        CyclicChild cyclicChild = new CyclicChild();
        cyclicChild.parent = cyclicParent;
        cyclicChild.date = simpleDateFormat.parse("1980-01-02");
        cyclicParent.id = 1;
        cyclicParent.childList = Collections.singletonList(cyclicChild);

        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("createCyclicObjects")
                .arguments(Collections.emptyList())
                .result(cyclicParent)
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
                        "import com.onushi.sample.model.CyclicParent;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void createCyclicObjects() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        CyclicParent result = sampleService.createCyclicObjects();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(1, result.childList.size());\n" +
                        "        assertEquals(simpleDateFormat.parse(\"1980-01-02 00:00:00.000\"), result.childList.get(0).date);\n" +
                        "        assertEquals(1, result.id);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }
}
