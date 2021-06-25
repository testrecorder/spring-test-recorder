/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.sample.model.Color;
import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.codegenerator.test.TestGenerator;
import com.onushi.springtestrecorder.codegenerator.test.TestGeneratorFactory;
import com.onushi.springtestrecorder.codegenerator.test.TestRecordingMoment;
import com.onushi.springtestrecorder.utils.ServiceCreatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

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
