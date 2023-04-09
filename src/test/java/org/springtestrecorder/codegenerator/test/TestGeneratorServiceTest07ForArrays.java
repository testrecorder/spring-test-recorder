/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.sample.services.SampleService;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest07ForArrays extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final boolean[] boolArray = { true, false };
        final byte[] byteArray = { 1, 2 };
        final char[] charArray = { 'a', 'z' };
        final double[] doubleArray = { 1.0, 2.0 };
        final float[] floatArray = { 1.0f, 2.0f };
        final int[] intArray = { 3, 4 };
        final long[] longArray = { 3L, 4L };
        final short[] shortArray = { 3, 4 };
        final String[] stringArray = { "a", "z" };
        final Object[] objectArray = { "a", 2 };

        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
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
        final String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                "\n" +
                "package org.sample.services;\n" +
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
        final boolean[] array1 = { true, false };

        final byte[] array2 = { (byte) 1, (byte) 2 };

        final char[] array3 = { 'a', 'z' };

        final double[] array4 = { 1.0, 2.0 };

        final float[] array5 = { 1.0f, 2.0f };

        final int[] array6 = { 3, 4 };

        final long[] array7 = { 3L, 4L };

        final short[] array8 = { (short) 3, (short) 4 };

        final String[] array9 = { "a", "z" };

        final Object[] array10 = { "a", 2 };

        final SampleService sampleService = new SampleService();

        // Act
        final Integer result = sampleService.processArrays(array1, array2, array3, array4, array5, array6, array7, array8, array9, array10);

        // Assert
        assertEquals(42, result);
    }
}
