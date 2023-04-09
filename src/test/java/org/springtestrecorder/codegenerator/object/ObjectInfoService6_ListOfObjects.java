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
import org.sample.services.SampleService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectInfoService6_ListOfObjects {
    private ObjectInfoService objectInfoService;
    private TestGeneratorFactory testGeneratorFactory;
    private ObjectInfoFactoryManager objectInfoFactoryManager;
    private List<Object> list;

    @BeforeEach
    protected void setUp() {
        testGeneratorFactory = ServiceCreatorUtils.createTestGeneratorFactory();
        objectInfoFactoryManager = ServiceCreatorUtils.createObjectInfoFactoryManager();
        objectInfoService = new ObjectInfoService();

        ObjectInfoCreationContext objectInfoCreationContext1 = new ObjectInfoCreationContext();
        objectInfoCreationContext1.setObject(new Object());

        list = new ArrayList<>();
        list.add("a");
        list.add(1);
        list.add(null);
    }

    @Test
    void testObjectList() {
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("getFirstName")
                .arguments(Collections.singletonList(list))
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());

        ObjectInfo objectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator, list);
        assertTrue(objectInfoService.objectInfoEquivalent(objectInfo, TestRecordingMoment.FIRST_SNAPSHOT, objectInfo, TestRecordingMoment.LAST_SNAPSHOT));
        assertTrue(objectInfoService.objectInfoEquivalent(objectInfo, TestRecordingMoment.LAST_SNAPSHOT, objectInfo, TestRecordingMoment.FIRST_SNAPSHOT));
    }

    @Test
    void testNewProperty() throws ParseException {
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("getFirstName")
                .arguments(Collections.singletonList(list))
                .build());
        list.clear();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2021-01-01");
        list.add(date);
        list.add(null);
        list.add(1);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());

        ObjectInfo objectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator, list);
        assertFalse(objectInfoService.objectInfoEquivalent(objectInfo, TestRecordingMoment.FIRST_SNAPSHOT, objectInfo, TestRecordingMoment.LAST_SNAPSHOT));
        assertFalse(objectInfoService.objectInfoEquivalent(objectInfo, TestRecordingMoment.LAST_SNAPSHOT, objectInfo, TestRecordingMoment.FIRST_SNAPSHOT));
    }

}
