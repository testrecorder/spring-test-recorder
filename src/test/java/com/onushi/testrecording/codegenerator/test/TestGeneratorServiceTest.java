package com.onushi.testrecording.codegenerator.test;

import com.onushi.sampleapp.SampleService;
import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.analizer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGeneratorFactory;
import com.onushi.testrecording.sampleclasses.Student;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TestGeneratorServiceTest {
    TestGeneratorFactory testGeneratorFactory;
    TestGeneratorService testGeneratorService;

    @BeforeEach
    void setUp() {
        ClassInfoService classInfoService = new ClassInfoService();
        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(classInfoService, new ObjectStateReaderService(objectNameGenerator));
        testGeneratorFactory = new TestGeneratorFactory(objectCodeGeneratorFactory, objectNameGenerator, classInfoService);
        testGeneratorService = new TestGeneratorService(classInfoService);
    }

    @Test
    void generateTestForAddFloats() throws Exception {
        // Arrange
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("addFloats")
                .arguments(Arrays.asList(2f, 3f))
                .result(5f)
                .resultType(float.class)
                .build();
        TestGenenerator testGenenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
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
                        "        float result = sampleService.addFloats(2.0f, 3.0f);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(5.0f, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }

    @Test
    void generateTestForMinDate() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = simpleDateFormat.parse("2021-01-01");
        Date d2 = simpleDateFormat.parse("2021-02-02");
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("minDate")
                .arguments(Arrays.asList(d1, d2))
                .result(d1)
                .resultType(Date.class)
                .build();
        TestGenenerator testGenenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
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
                        "        assertEquals(expectedResult, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));
    }

    @Test
    void generateTestForReturnNull() throws Exception {
        // Arrange
        MethodRunInfo methodRunInfo = MethodRunInfo.builder()
                .target(new SampleService())
                .methodName("returnNull")
                .arguments(Collections.emptyList())
                .result(null)
                .resultType(Student.class)
                .build();
        TestGenenerator testGenenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenenerator);

        // Assert
        assertEquals(StringUtils.trimAndIgnoreCRDiffs("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sampleapp;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        "    @Test\n" +
                        "    void returnNull() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Student result = sampleService.returnNull();\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(null, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.trimAndIgnoreCRDiffs(testString));

    }
}
