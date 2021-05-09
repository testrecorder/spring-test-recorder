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

    public TestGeneratorFactory(ObjectNameGenerator objectNameGenerator,
                                ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager) {
        this.objectNameGenerator = objectNameGenerator;
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
    }

    public TestGenerator createTestGenerator(RecordedMethodRunInfo recordedMethodRunInfo) {
        TestGenerator testGenerator = new TestGenerator();

        if (recordedMethodRunInfo.getArguments() == null) {
            throw new IllegalArgumentException("arguments");
        }
        if (recordedMethodRunInfo.getTarget() == null) {
            throw new IllegalArgumentException("target");
        }

        testGenerator.targetObjectCodeGenerator = objectCodeGeneratorFactoryManager.getNamedObjectCodeGenerator(testGenerator,
                recordedMethodRunInfo.getTarget(),
                objectNameGenerator.getBaseObjectName(recordedMethodRunInfo.getTarget()));
        testGenerator.packageName = recordedMethodRunInfo.getTarget().getClass().getPackage().getName();
        testGenerator.shortClassName = recordedMethodRunInfo.getTarget().getClass().getSimpleName();
        testGenerator.methodName = recordedMethodRunInfo.getMethodName();

        testGenerator.argumentObjectCodeGenerators = recordedMethodRunInfo.getArguments().stream()
                .map(x -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(testGenerator, x))
                .collect(Collectors.toList());

        testGenerator.expectedResultObjectCodeGenerator = objectCodeGeneratorFactoryManager.getNamedObjectCodeGenerator(testGenerator,
                recordedMethodRunInfo.getResult(), "expectedResult");
        testGenerator.expectedException = recordedMethodRunInfo.getException();

        testGenerator.requiredImports = getRequiredImports(testGenerator);

        testGenerator.requiredHelperObjects = getRequiredHelperObjects(testGenerator);

        // TODO IB !!!! write a test for this
        List<ObjectCodeGenerator> objectsToInit = new ArrayList<>(testGenerator.argumentObjectCodeGenerators);
        objectsToInit.add(testGenerator.targetObjectCodeGenerator);
        testGenerator.objectsInit = getObjectsInit(objectsToInit);

        testGenerator.argumentsInlineCode = getArgumentsInlineCode(testGenerator);

        testGenerator.expectedResultInit = getObjectsInit(Collections.singletonList(testGenerator.expectedResultObjectCodeGenerator));

        testGenerator.resultDeclareClassName = getResultDeclareClassName(testGenerator.expectedResultObjectCodeGenerator, recordedMethodRunInfo.getFallBackResultType());

        return testGenerator;
    }

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

    private List<String> getObjectsInit(List<ObjectCodeGenerator> objectCodeGenerators) {
        Set<String> objectInitsAlreadyAdded = new HashSet<>();

        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectCodeGenerator argumentObjectCodeGenerator : objectCodeGenerators) {
            List<String> objectsInit = getObjectsInit(argumentObjectCodeGenerator);
            for (String objectInit : objectsInit) {
                if (objectInitsAlreadyAdded.add(objectInit)) {
                    allObjectsInit.add(objectInit);
                }
            }
        }
        return allObjectsInit;
    }

    private List<String> getObjectsInit(ObjectCodeGenerator objectCodeGenerator) {
        if (objectCodeGenerator.isInitPrepared() || objectCodeGenerator.isInitDone()) {
            // to avoid cyclic traversal
            return new ArrayList<>();
        }
        objectCodeGenerator.setInitPrepared(true);
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectCodeGenerator dependency : objectCodeGenerator.getDependencies()) {
            allObjectsInit.addAll(getObjectsInit(dependency));
        }
        if (!objectCodeGenerator.getInitCode().equals("")) {
            allObjectsInit.add(objectCodeGenerator.getInitCode());
        }
        objectCodeGenerator.setInitDone(true);
        return allObjectsInit;
    }

    private List<String> getArgumentsInlineCode(TestGenerator testGenerator) {
        return testGenerator.argumentObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInlineCode)
                .collect(Collectors.toList());
    }
}
