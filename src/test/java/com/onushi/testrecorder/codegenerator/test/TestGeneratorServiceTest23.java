package com.onushi.testrecorder.codegenerator.test;

import com.onushi.sample.model.CyclicChild;
import com.onushi.sample.model.CyclicParent;
import com.onushi.sample.services.SampleService;
import com.onushi.testrecorder.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest23 extends TestGeneratorServiceTest {
    @Test
    void generateTestForCyclicDependenciesInArgs() throws Exception {
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
                .methodName("processCyclicObjects")
                .arguments(Collections.singletonList(cyclicParent))
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
                        "import com.onushi.sample.model.CyclicParent;\n" +
                        "import java.util.List;\n" +
                        "import com.onushi.sample.model.CyclicChild;\n" +
                        "import java.util.Arrays;\n" +
                        "import java.util.Date;\n" +
                        "import java.text.SimpleDateFormat;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processCyclicObjects() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                        "\n" +
                        "        Date date1 = simpleDateFormat.parse(\"1980-01-02 00:00:00.000\");\n" +
                        "        // TODO Solve initialisation for cyclic dependency in com.onushi.sample.model.CyclicParent\n" +
                        "        CyclicChild cyclicChild1 = new CyclicChild();\n" +
                        "        cyclicChild1.date = date1;\n" +
                        "        cyclicChild1.parent = ...;\n" +
                        "        List<CyclicChild> singletonList1 = Arrays.asList(cyclicChild1);\n" +
                        "\n" +
                        "        CyclicParent cyclicParent1 = new CyclicParent();\n" +
                        "        cyclicParent1.childList = singletonList1;\n" +
                        "        cyclicParent1.id = 1;\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.processCyclicObjects(cyclicParent1);\n" +
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