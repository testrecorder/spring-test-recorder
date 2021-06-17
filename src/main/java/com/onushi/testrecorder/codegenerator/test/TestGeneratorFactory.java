package com.onushi.testrecorder.codegenerator.test;

import com.onushi.testrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.testrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.testrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.testrecorder.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecorder.codegenerator.object.ObjectInfo;
import com.onushi.testrecorder.codegenerator.object.ObjectInfoFactoryManager;
import org.springframework.stereotype.Service;

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
        testGenerator.dependencyMethodRuns.add(dependencyMethodRunInfo);
    }

    public void addAfterMethodRunInfo(TestGenerator testGenerator, AfterMethodRunInfo afterMethodRunInfo) {
        testGenerator.expectedResultObjectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator,
                afterMethodRunInfo.getResult());

        testGenerator.expectedException = afterMethodRunInfo.getException();

        testGenerator.resultDeclareClassName = getResultDeclareClassName(testGenerator.expectedResultObjectInfo, testGenerator.fallBackResultType);
    }


    // TODO IB !!!! obsolete
    public TestGenerator createTestGenerator(RecordedMethodRunInfo recordedMethodRunInfo) {
        TestGenerator testGenerator = new TestGenerator();

        if (recordedMethodRunInfo.getArguments() == null) {
            throw new IllegalArgumentException("arguments");
        }
        if (recordedMethodRunInfo.getTarget() == null) {
            throw new IllegalArgumentException("target");
        }

        testGenerator.packageName = recordedMethodRunInfo.getTarget().getClass().getPackage().getName();
        testGenerator.shortClassName = recordedMethodRunInfo.getTarget().getClass().getSimpleName();

        // TODO IB !!!! this info is available during method run only. Mock should be postponed
        testGenerator.dependencyMethodRuns = recordedMethodRunInfo.getDependencyMethodRuns();

        testGenerator.targetObjectInfo = objectInfoFactoryManager.getNamedObjectInfo(testGenerator,
                recordedMethodRunInfo.getTarget(),
                objectNameGenerator.getBaseObjectName(recordedMethodRunInfo.getTarget()));
        testGenerator.methodName = recordedMethodRunInfo.getMethodName();

        testGenerator.argumentObjectInfos = recordedMethodRunInfo.getArguments().stream()
                .map(x -> objectInfoFactoryManager.getCommonObjectInfo(testGenerator, x))
                .collect(Collectors.toList());


        testGenerator.expectedResultObjectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator,
                recordedMethodRunInfo.getResult());
        testGenerator.expectedException = recordedMethodRunInfo.getException();

        testGenerator.resultDeclareClassName = getResultDeclareClassName(testGenerator.expectedResultObjectInfo, recordedMethodRunInfo.getFallBackResultType());

        return testGenerator;
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
