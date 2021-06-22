package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.model.Color;
import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest36SendTargetAsArgWithSideEffects extends TestGeneratorServiceTest {
    // TODO IB !!!! activate
//    @Test
//    void generateTest() {
//        // Arrange
//        // actually you cannot change the value of a enum it will be a different object
//        SampleService sampleService = new SampleService();
//        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
//                .target(sampleService)
//                .methodName("changeSampleService")
//                .arguments(Collections.singletonList(sampleService))
//                .fallBackResultType(void.class)
//                .build());
//        sampleService.changeSampleService(sampleService);
//        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
//                .result(null)
//                .build());
//
//        // Act
//        String testString = testGeneratorService.generateTestCode(testGenerator);
//
//        // Assert
//        assertEquals(StringUtils.prepareForCompare(""),
//                StringUtils.prepareForCompare(testString));
//    }
}
