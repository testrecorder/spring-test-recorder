/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.object;

import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.codegenerator.test.TestGenerator;
import org.springtestrecorder.codegenerator.test.TestGeneratorFactory;
import org.springtestrecorder.codegenerator.test.TestRecordingMoment;
import org.springtestrecorder.utils.ServiceCreatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sample.model.Color;
import org.sample.services.SampleService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectInfoService2_EnumObject {
    private ObjectInfoService objectInfoService;
    private ObjectInfo objectInfo;

    @BeforeEach
    protected void setUp() {
        TestGeneratorFactory testGeneratorFactory = ServiceCreatorUtils.createTestGeneratorFactory();
        ObjectInfoFactoryManager objectInfoFactoryManager = ServiceCreatorUtils.createObjectInfoFactoryManager();
        objectInfoService = new ObjectInfoService();

        Color color = Color.BLUE;

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("dummyMethod")
                .arguments(Collections.singletonList(color))
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());
        objectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator, color);
    }

    @Test
    void testSameObjectSameSnapshot() {
        assertTrue(objectInfoService.objectInfoEquivalent(objectInfo, TestRecordingMoment.FIRST_SNAPSHOT, objectInfo, TestRecordingMoment.FIRST_SNAPSHOT));
        assertTrue(objectInfoService.objectInfoEquivalent(objectInfo, TestRecordingMoment.LAST_SNAPSHOT, objectInfo, TestRecordingMoment.LAST_SNAPSHOT));
    }

    @Test
    void testNullObjectInfo() {
        assertTrue(objectInfoService.objectInfoEquivalent(null, TestRecordingMoment.LAST_SNAPSHOT, null, TestRecordingMoment.LAST_SNAPSHOT));
        assertFalse(objectInfoService.objectInfoEquivalent(objectInfo, TestRecordingMoment.LAST_SNAPSHOT, null, TestRecordingMoment.LAST_SNAPSHOT));
        assertFalse(objectInfoService.objectInfoEquivalent(null, TestRecordingMoment.LAST_SNAPSHOT, objectInfo, TestRecordingMoment.LAST_SNAPSHOT));
    }

    @Test
    void testChanges() {
        assertTrue(objectInfoService.objectInfoEquivalent(objectInfo, TestRecordingMoment.FIRST_SNAPSHOT, objectInfo, TestRecordingMoment.LAST_SNAPSHOT));
        assertTrue(objectInfoService.objectInfoEquivalent(objectInfo, TestRecordingMoment.LAST_SNAPSHOT, objectInfo, TestRecordingMoment.FIRST_SNAPSHOT));
    }
}
