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
        testGenerator.threadId = beforeMethodRunInfo.getThreadId();

        // TODO IB !!!! 1 require threadId.
        // TODO IB !!!! 1 check the thread in testGeneratorFactory.addDependencyMethodRun and add in all mocking tests
        if (beforeMethodRunInfo.getArguments() == null) {
            throw new IllegalArgumentException("arguments");
        }
        if (beforeMethodRunInfo.getTarget() == null) {
            throw new IllegalArgumentException("target");
        }

        testGenerator.packageName = beforeMethodRunInfo.getTarget().getClass().getPackage().getName();
        testGenerator.shortClassName = beforeMethodRunInfo.getTarget().getClass().getSimpleName();

        testGenerator.targetObjectInfo = objectInfoFactoryManager.getNamedObjectInfo(testGenerator,
                beforeMethodRunInfo.getTarget(),
                objectNameGenerator.getBaseObjectName(beforeMethodRunInfo.getTarget()));
        testGenerator.methodName = beforeMethodRunInfo.getMethodName();

        testGenerator.argumentObjectInfos = beforeMethodRunInfo.getArguments().stream()
                .map(x -> objectInfoFactoryManager.getCommonObjectInfo(testGenerator, x))
                .collect(Collectors.toList());

        testGenerator.fallBackResultType = beforeMethodRunInfo.getFallBackResultType();

        return testGenerator;
    }

    public void addDependencyMethodRun(TestGenerator testGenerator, DependencyMethodRunInfo dependencyMethodRunInfo) {
        // TODO IB LATER this check should be optional
        // TODO IB !!!! 1 check thread
        testGenerator.dependencyMethodRuns.add(dependencyMethodRunInfo);
    }

    public void addAfterMethodRunInfo(TestGenerator testGenerator, AfterMethodRunInfo afterMethodRunInfo) {
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
