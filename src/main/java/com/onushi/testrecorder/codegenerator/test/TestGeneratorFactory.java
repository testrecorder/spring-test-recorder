package com.onushi.testrecorder.codegenerator.test;

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

    // TODO IB !!!! 3 create in 2 steps from RecordedMethodRunInfoBefore and RecordedMethodRunInfoAfter
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
        testGenerator.targetObjectInfo = objectInfoFactoryManager.getNamedObjectInfo(testGenerator,
                recordedMethodRunInfo.getTarget(),
                objectNameGenerator.getBaseObjectName(recordedMethodRunInfo.getTarget()));
        testGenerator.methodName = recordedMethodRunInfo.getMethodName();

        testGenerator.argumentObjectInfos = recordedMethodRunInfo.getArguments().stream()
                .map(x -> objectInfoFactoryManager.getCommonObjectInfo(testGenerator, x))
                .collect(Collectors.toList());

        testGenerator.dependencyMethodRuns = recordedMethodRunInfo.getDependencyMethodRuns();

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
