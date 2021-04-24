package com.onushi.testrecording.codegenerator.test;

import com.onushi.sampleapp.SampleService;
import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGeneratorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TestGeneratorServiceTest {
    TestGeneratorFactory testGeneratorFactory;
    TestGeneratorService testGeneratorService;

    @BeforeEach
    void setUp() {
        ClassInfoService classInfoService = new ClassInfoService();
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(classInfoService);
        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator(classInfoService);
        testGeneratorFactory = new TestGeneratorFactory(objectCodeGeneratorFactory, objectNameGenerator, classInfoService);
        testGeneratorService = new TestGeneratorService(classInfoService);
    }

    @Test
    void generateTestForAddFloats() {
        // Arrange
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("addFloats")
                .arguments(Arrays.asList(2f, 3f))
                .result(5f)
                .build();
        TestGenenerator testGenenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenenerator);

        // Assert
        assertEquals(ignoreLineSeparation(testString),
                ignoreLineSeparation("BEGIN GENERATED TEST =========\n" +
                "\n" +
                "package com.onushi.sampleapp;\n" +
                "\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import static org.junit.jupiter.api.Assertions.*;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                "    @Test\n" +
                "    void addFloats() throws Exception {\n" +
                "        // Arrange\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        Float result = sampleService.addFloats(2.0f, 3.0f);\n" +
                "\n" +
                "        // Assert\n" +
                "        assertEquals(result, 5.0f);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="));
    }

    @Test
    void generateTestForMinDate() throws ParseException {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = simpleDateFormat.parse("2021-01-01");
        Date d2 = simpleDateFormat.parse("2021-02-02");
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("minDate")
                .arguments(Arrays.asList(d1, d2))
                .result(d1)
                .build();
        TestGenenerator testGenenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenenerator);

        // Assert
        assertEquals(ignoreLineSeparation(testString),
                ignoreLineSeparation("BEGIN GENERATED TEST =========\n" +
                "\n" +
                "package com.onushi.sampleapp;\n" +
                "\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import static org.junit.jupiter.api.Assertions.*;\n" +
                "import java.text.SimpleDateFormat;\n" +
                "import java.util.Date;\n" +
                "\n" +
                "class SampleServiceTest {\n" +
                "    @Test\n" +
                "    void minDate() throws Exception {\n" +
                "        // Arrange\n" +
                "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n" +
                "        Date date1 = simpleDateFormat.parse(\"2021-01-01 00:00:00.000\");\n" +
                "        Date date2 = simpleDateFormat.parse(\"2021-02-02 00:00:00.000\");\n" +
                "        SampleService sampleService = new SampleService();\n" +
                "\n" +
                "        // Act\n" +
                "        Date result = sampleService.minDate(date1, date2);\n" +
                "\n" +
                "        // Assert\n" +
                "        Date expectedResult = simpleDateFormat.parse(\"2021-01-01 00:00:00.000\");\n" +
                "        assertEquals(result, expectedResult);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="));
    }

    private String ignoreLineSeparation(String input) {
        return input.trim().replace("\n", "").replace("\r", "");
    }
}
