package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.object.ObjectInfoFactoryManager;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TestGeneratorFactory {
    private final ObjectNameGenerator objectNameGenerator;
    private final ObjectInfoFactoryManager objectInfoFactoryManager;
    private final TestGeneratorService testGeneratorService;

    public TestGeneratorFactory(ObjectNameGenerator objectNameGenerator,
                                ObjectInfoFactoryManager objectInfoFactoryManager,
                                TestGeneratorService testGeneratorService) {
        this.objectNameGenerator = objectNameGenerator;
        this.objectInfoFactoryManager = objectInfoFactoryManager;
        this.testGeneratorService = testGeneratorService;
    }

    public TestGenerator createTestGenerator(RecordedMethodRunInfo recordedMethodRunInfo) {
        TestGenerator testGenerator = new TestGenerator();

        if (recordedMethodRunInfo.getArguments() == null) {
            throw new IllegalArgumentException("arguments");
        }
        if (recordedMethodRunInfo.getTarget() == null) {
            throw new IllegalArgumentException("target");
        }

        testGenerator.dependencyMethodRuns = recordedMethodRunInfo.getDependencyMethodRuns();
        testGenerator.targetObjectInfo = objectInfoFactoryManager.getNamedObjectInfo(testGenerator,
                recordedMethodRunInfo.getTarget(),
                objectNameGenerator.getBaseObjectName(recordedMethodRunInfo.getTarget()));
        testGenerator.packageName = recordedMethodRunInfo.getTarget().getClass().getPackage().getName();
        testGenerator.shortClassName = recordedMethodRunInfo.getTarget().getClass().getSimpleName();
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
