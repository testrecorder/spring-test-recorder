/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.springtestrecorder.codegenerator.object.ObjectInfo;
import com.onushi.springtestrecorder.codegenerator.object.ObjectInfoFactoryManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class TestGeneratorFactory {
    private final ObjectNameGenerator objectNameGenerator;
    private final ObjectInfoFactoryManager objectInfoFactoryManager;

    public TestGeneratorFactory(ObjectNameGenerator objectNameGenerator,
                                ObjectInfoFactoryManager objectInfoFactoryManager) {
        this.objectNameGenerator = objectNameGenerator;
        this.objectInfoFactoryManager = objectInfoFactoryManager;
    }

    public TestGenerator createTestGenerator(BeforeMethodRunInfo beforeMethodRunInfo) {
        TestGenerator testGenerator = new TestGenerator();
        testGenerator.threadId = Thread.currentThread().getId();

        if (beforeMethodRunInfo.getArguments() == null) {
            throw new IllegalArgumentException("arguments");
        }
        if (beforeMethodRunInfo.getTarget() == null) {
            throw new IllegalArgumentException("target");
        }

        testGenerator.packageName = beforeMethodRunInfo.getTarget().getClass().getPackage().getName();
        testGenerator.shortClassName = beforeMethodRunInfo.getTarget().getClass().getSimpleName();

        testGenerator.targetObjectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator,
                beforeMethodRunInfo.getTarget(),
                objectNameGenerator.getBaseObjectName(beforeMethodRunInfo.getTarget()));
        testGenerator.methodName = beforeMethodRunInfo.getMethodName();

        testGenerator.argumentObjectInfos = beforeMethodRunInfo.getArguments().stream()
                .map(x -> objectInfoFactoryManager.getCommonObjectInfo(testGenerator, x))
                .collect(Collectors.toList());

        testGenerator.fallBackResultType = beforeMethodRunInfo.getFallBackResultType();

        testGenerator.currentTestRecordingPhase = TestRecordingPhase.DURING_METHOD_RUN;
        return testGenerator;
    }

    public void addDependencyMethodRun(TestGenerator testGenerator, DependencyMethodRunInfo dependencyMethodRunInfo) {
        if (testGenerator.getThreadId() == dependencyMethodRunInfo.getThreadId()) {
            testGenerator.dependencyMethodRuns.add(dependencyMethodRunInfo);
        }
    }

    public void addAfterMethodRunInfo(TestGenerator testGenerator, AfterMethodRunInfo afterMethodRunInfo) {
        testGenerator.currentTestRecordingPhase = TestRecordingPhase.AFTER_METHOD_RUN;
        testGenerator.expectedResultObjectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator,
                afterMethodRunInfo.getResult());

        testGenerator.expectedException = afterMethodRunInfo.getException();

        testGenerator.resultDeclareClassName = getResultDeclareClassName(testGenerator.expectedResultObjectInfo, testGenerator.fallBackResultType);

        ArrayList<ObjectInfo> objectInfos = new ArrayList<>(testGenerator.objectInfoCache.values());
        for (ObjectInfo objectInfo : objectInfos) {
            if (objectInfo.getToRunAfterMethodRun() != null) {
                objectInfo.getToRunAfterMethodRun().run();
            }
        }
    }

    private String getResultDeclareClassName(ObjectInfo expectedResultObjectInfo, Class<?> fallBackResultType) {
        if (expectedResultObjectInfo.getObject() != null) {
            return expectedResultObjectInfo.getComposedClassNameForDeclare();
        }
        if (fallBackResultType != null) {
            if (fallBackResultType.isPrimitive()) {
                return fallBackResultType.getCanonicalName();
            } else {
                return fallBackResultType.getSimpleName();
            }
        }
        return "Object";
    }
}
