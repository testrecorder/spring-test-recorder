/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import org.springtestrecorder.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest07ForArrays extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        boolean[] boolArray = {true, false};
        byte[] byteArray = {1, 2};
        char[] charArray = {'a', 'z'};
        double[] doubleArray = {1.0, 2.0};
        float[] floatArray = {1.0f, 2.0f};
        int[] intArray = {3, 4};
        long[] longArray = {3L, 4L};
        short[] shortArray = {3, 4};
        String[] stringArray = {"a", "z"};
        Object[] objectArray = {"a", 2};

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("processArrays")
                .arguments(Arrays.asList(boolArray, byteArray, charArray, doubleArray,
                        floatArray, intArray, longArray, shortArray, stringArray, objectArray))
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(42)
                .exception(null)
                .build());

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package org.springtestrecorder.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void processArrays() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        boolean[] array1 = {true, false};\n" +
                        "\n" +
                        "        byte[] array2 = {(byte)1, (byte)2};\n" +
                        "\n" +
                        "        char[] array3 = {'a', 'z'};\n" +
                        "\n" +
                        "        double[] array4 = {1.0, 2.0};\n" +
                        "\n" +
                        "        float[] array5 = {1.0f, 2.0f};\n" +
                        "\n" +
                        "        int[] array6 = {3, 4};\n" +
                        "\n" +
                        "        long[] array7 = {3L, 4L};\n" +
                        "\n" +
                        "        short[] array8 = {(short)3, (short)4};\n" +
                        "\n" +
                        "        String[] array9 = {\"a\", \"z\"};\n" +
                        "\n" +
                        "        Object[] array10 = {\"a\", 2};\n" +
                        "\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.processArrays(array1, array2, array3, array4, array5, array6, array7, array8, array9, array10);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void processArrays() {
        // Arrange
        boolean[] array1 = {true, false};

        byte[] array2 = {(byte)1, (byte)2};

        char[] array3 = {'a', 'z'};

        double[] array4 = {1.0, 2.0};

        float[] array5 = {1.0f, 2.0f};

        int[] array6 = {3, 4};

        long[] array7 = {3L, 4L};

        short[] array8 = {(short)3, (short)4};

        String[] array9 = {"a", "z"};

        Object[] array10 = {"a", 2};

        SampleService sampleService = new SampleService();

        // Act
        Integer result = sampleService.processArrays(array1, array2, array3, array4, array5, array6, array7, array8, array9, array10);

        // Assert
        assertEquals(42, result);
    }
}
