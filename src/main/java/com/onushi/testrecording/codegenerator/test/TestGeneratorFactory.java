package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGenerator;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGeneratorFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class TestGeneratorFactory {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;
    private final ObjectNameGenerator objectNameGenerator;
    private final ClassInfoService classInfoService;

    public TestGeneratorFactory(ObjectCodeGeneratorFactory objectCodeGeneratorFactory, ObjectNameGenerator objectNameGenerator, ClassInfoService classInfoService) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
        this.objectNameGenerator = objectNameGenerator;
        this.classInfoService = classInfoService;
    }

    public TestGenenerator createTestGenerator(MethodRunInfo methodRunInfo) {
        TestGenenerator testGenenerator = new TestGenenerator();

        testGenenerator.targetObjectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(methodRunInfo.getTarget(),
                classInfoService.getObjectNameBase(methodRunInfo.getTarget()));
        testGenenerator.packageName = classInfoService.getPackageName(methodRunInfo.getTarget());
        testGenenerator.shortClassName = classInfoService.getShortClassName(methodRunInfo.getTarget());
        testGenenerator.methodName = methodRunInfo.getMethodName();

        testGenenerator.argumentObjectCodeGenerators = methodRunInfo.getArguments().stream()
                .map(x -> objectCodeGeneratorFactory.createObjectCodeGenerator(x, objectNameGenerator.generateObjectName(testGenenerator, x)))
                .collect(Collectors.toList());

        testGenenerator.resultObjectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(methodRunInfo.getResult(), "expectedResult");


        setRequiredImports(testGenenerator);

        setRequiredHelperObjects(testGenenerator);

        setObjectsInit(testGenenerator);

        setArgumentsInlineCode(testGenenerator);

        testGenenerator.resultInit = testGenenerator.resultObjectCodeGenerator.getInitCode();

        return testGenenerator;
    }

    private void setRequiredImports(TestGenenerator testGenenerator) {
        testGenenerator.requiredImports = new ArrayList<>();
        testGenenerator.requiredImports.add("org.junit.jupiter.api.Test");
        testGenenerator.requiredImports.add("static org.junit.jupiter.api.Assertions.*");
        testGenenerator.requiredImports.addAll(testGenenerator.argumentObjectCodeGenerators.stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        testGenenerator.requiredImports.addAll(testGenenerator.resultObjectCodeGenerator.getRequiredImports());
        testGenenerator.requiredImports = testGenenerator.requiredImports.stream().distinct().collect(Collectors.toList());
    }

    private void setRequiredHelperObjects(TestGenenerator testGenenerator) {
        testGenenerator.requiredHelperObjects = testGenenerator.argumentObjectCodeGenerators.stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        testGenenerator.requiredHelperObjects.addAll(testGenenerator.resultObjectCodeGenerator.getRequiredHelperObjects());
        testGenenerator.requiredHelperObjects = testGenenerator.requiredHelperObjects.stream().distinct().collect(Collectors.toList());
    }

    private void setObjectsInit(TestGenenerator testGenenerator) {
        testGenenerator.objectsInit = testGenenerator.argumentObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInitCode).collect(Collectors.toList());
        testGenenerator.objectsInit = testGenenerator.objectsInit.stream()
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());
    }

    private void setArgumentsInlineCode(TestGenenerator testGenenerator) {
        testGenenerator.argumentsInlineCode = testGenenerator.argumentObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInlineCode)
                .collect(Collectors.toList());
    }
}
