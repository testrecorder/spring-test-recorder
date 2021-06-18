package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest28SideEffectsOnArrayLists extends TestGeneratorServiceTest {
    @Test
    void generateTestWithSideEffectsOnArrayLists() {
        // Arrange
        int[] intArray = {3, 4};
        List<Float> floatList = new ArrayList<>();
        floatList.add(1f);
        floatList.add(2f);

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("repeatedArgs")
                .arguments(Arrays.asList(intArray, floatList))
                .build());
        floatList.add(0, 0f);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(42)
                .build());

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare(""),
                StringUtils.prepareForCompare(testString));
    }
}
