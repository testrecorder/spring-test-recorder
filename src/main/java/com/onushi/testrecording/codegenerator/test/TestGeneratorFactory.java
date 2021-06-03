package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGenerator;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGeneratorFactoryManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestGeneratorFactory {
    private final ObjectNameGenerator objectNameGenerator;
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;
    private final TestGeneratorService testGeneratorService;

    public TestGeneratorFactory(ObjectNameGenerator objectNameGenerator,
                                ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager,
                                TestGeneratorService testGeneratorService) {
        this.objectNameGenerator = objectNameGenerator;
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
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
        testGenerator.targetObjectCodeGenerator = objectCodeGeneratorFactoryManager.getNamedObjectCodeGenerator(testGenerator,
                recordedMethodRunInfo.getTarget(),
                objectNameGenerator.getBaseObjectName(recordedMethodRunInfo.getTarget()));
        testGenerator.packageName = recordedMethodRunInfo.getTarget().getClass().getPackage().getName();
        testGenerator.shortClassName = recordedMethodRunInfo.getTarget().getClass().getSimpleName();
        testGenerator.methodName = recordedMethodRunInfo.getMethodName();

        testGenerator.argumentObjectCodeGenerators = recordedMethodRunInfo.getArguments().stream()
                .map(x -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(testGenerator, x))
                .collect(Collectors.toList());

        testGenerator.expectedResultObjectCodeGenerator = objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(testGenerator,
                recordedMethodRunInfo.getResult());
        testGenerator.expectedException = recordedMethodRunInfo.getException();

        testGenerator.requiredImports = getRequiredImports(testGenerator);

        testGenerator.requiredHelperObjects = getRequiredHelperObjects(testGenerator);

        testGenerator.argumentsInlineCode = getArgumentsInlineCode(testGenerator);

        testGenerator.resultDeclareClassName = getResultDeclareClassName(testGenerator.expectedResultObjectCodeGenerator, recordedMethodRunInfo.getFallBackResultType());

        return testGenerator;
    }

    // TODO IB !!!! this is suspect. Could ObjectCodeGenerator handle this?
    private String getResultDeclareClassName(ObjectCodeGenerator expectedResultObjectCodeGenerator, Class<?> fallBackResultType) {
        if (expectedResultObjectCodeGenerator.getObject() != null) {
            return expectedResultObjectCodeGenerator.getDeclareClassName();
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

    private List<String> getRequiredImports(TestGenerator testGenerator) {
        List<String> result = new ArrayList<>();
        result.add("org.junit.jupiter.api.Test");
        result.add("static org.junit.jupiter.api.Assertions.*");
        result.addAll(testGenerator.getObjectCodeGeneratorCache().values().stream()
                .flatMap(x -> x.getRequiredImports().stream())
                .collect(Collectors.toList()));
        return result.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getRequiredHelperObjects(TestGenerator testGenerator) {
        return testGenerator.getObjectCodeGeneratorCache().values().stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getArgumentsInlineCode(TestGenerator testGenerator) {
        return testGenerator.argumentObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInlineCode)
                .collect(Collectors.toList());
    }
}
