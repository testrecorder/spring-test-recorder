package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGenerator;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGeneratorFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class TestGeneratorFactory {
    private final ObjectNameGenerator objectNameGenerator;
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public TestGeneratorFactory(ObjectNameGenerator objectNameGenerator,
                                ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.objectNameGenerator = objectNameGenerator;
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    public TestGenerator createTestGenerator(MethodRunInfo methodRunInfo) throws Exception {
        TestGenerator testGenerator = new TestGenerator();

        if (methodRunInfo.getArguments() == null) {
            throw new IllegalArgumentException("arguments");
        }
        if (methodRunInfo.getTarget() == null) {
            throw new IllegalArgumentException("target");
        }

        testGenerator.targetObjectCodeGenerator = objectCodeGeneratorFactory.getNamedObjectCodeGenerator(testGenerator,
                methodRunInfo.getTarget(),
                objectNameGenerator.getBaseObjectName(methodRunInfo.getTarget()));
        testGenerator.packageName = methodRunInfo.getTarget().getClass().getPackage().getName();
        testGenerator.shortClassName = methodRunInfo.getTarget().getClass().getSimpleName();
        testGenerator.methodName = methodRunInfo.getMethodName();

        testGenerator.argumentObjectCodeGenerators = methodRunInfo.getArguments().stream()
                .map(x -> objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, x))
                .collect(Collectors.toList());

        testGenerator.expectedResultObjectCodeGenerator = objectCodeGeneratorFactory.getNamedObjectCodeGenerator(testGenerator,
                methodRunInfo.getResult(), "expectedResult");
        testGenerator.resultType = methodRunInfo.getResultType();
        testGenerator.expectedException = methodRunInfo.getException();

        setRequiredImports(testGenerator);

        setRequiredHelperObjects(testGenerator);

        setObjectsInit(testGenerator);

        setArgumentsInlineCode(testGenerator);

        testGenerator.expectedResultInit = testGenerator.expectedResultObjectCodeGenerator.getInitCode();

        return testGenerator;
    }

    private void setRequiredImports(TestGenerator testGenerator) {
        testGenerator.requiredImports = new ArrayList<>();
        testGenerator.requiredImports.add("org.junit.jupiter.api.Test");
        testGenerator.requiredImports.add("static org.junit.jupiter.api.Assertions.*");
        testGenerator.requiredImports.addAll(testGenerator.argumentObjectCodeGenerators.stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        testGenerator.requiredImports.addAll(testGenerator.expectedResultObjectCodeGenerator.getRequiredImports());
        testGenerator.requiredImports = testGenerator.requiredImports.stream().distinct().collect(Collectors.toList());
    }

    private void setRequiredHelperObjects(TestGenerator testGenerator) {
        testGenerator.requiredHelperObjects = testGenerator.argumentObjectCodeGenerators.stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        testGenerator.requiredHelperObjects.addAll(testGenerator.expectedResultObjectCodeGenerator.getRequiredHelperObjects());
        testGenerator.requiredHelperObjects = testGenerator.requiredHelperObjects.stream().distinct().collect(Collectors.toList());
    }

    private void setObjectsInit(TestGenerator testGenerator) {
        testGenerator.objectsInit = testGenerator.argumentObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInitCode)
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());
    }

    private void setArgumentsInlineCode(TestGenerator testGenerator) {
        testGenerator.argumentsInlineCode = testGenerator.argumentObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInlineCode)
                .collect(Collectors.toList());
    }
}
