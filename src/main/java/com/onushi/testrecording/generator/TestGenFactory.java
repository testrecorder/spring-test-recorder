package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.generator.object.ObjectCodeGenerator;
import com.onushi.testrecording.generator.object.ObjectCodeGeneratorFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class TestGenFactory {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;
    private final ObjectNamesService objectNamesService;
    private final ClassInfoService classInfoService;

    public TestGenFactory(ObjectCodeGeneratorFactory objectCodeGeneratorFactory, ObjectNamesService objectNamesService, ClassInfoService classInfoService) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
        this.objectNamesService = objectNamesService;
        this.classInfoService = classInfoService;
    }

    public TestGenInfo createTestGenInfo(MethodRunInfo methodRunInfo) {
        TestGenInfo testGenInfo = new TestGenInfo();

        testGenInfo.targetObjectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(methodRunInfo.getTarget(),
                classInfoService.getObjectNameBase(methodRunInfo.getTarget()));
        testGenInfo.packageName = classInfoService.getPackageName(methodRunInfo.getTarget());
        testGenInfo.shortClassName = classInfoService.getShortClassName(methodRunInfo.getTarget());
        testGenInfo.methodName = methodRunInfo.getMethodName();

        testGenInfo.argumentObjectCodeGenerators = methodRunInfo.getArguments().stream()
                .map(x -> objectCodeGeneratorFactory.createObjectCodeGenerator(x, objectNamesService.generateObjectName(testGenInfo, x)))
                .collect(Collectors.toList());

        testGenInfo.resultObjectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(methodRunInfo.getResult(), "expectedResult");


        setRequiredImports(testGenInfo);

        setRequiredHelperObjects(testGenInfo);

        setObjectsInit(testGenInfo);

        setArgumentsInlineCode(testGenInfo);

        testGenInfo.resultInit = testGenInfo.resultObjectCodeGenerator.getInitCode();

        return testGenInfo;
    }

    private void setRequiredImports(TestGenInfo testGenInfo) {
        testGenInfo.requiredImports = new ArrayList<>();
        testGenInfo.requiredImports.add("org.junit.jupiter.api.Test");
        testGenInfo.requiredImports.add("static org.junit.jupiter.api.Assertions.*");
        testGenInfo.requiredImports.addAll(testGenInfo.argumentObjectCodeGenerators.stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        testGenInfo.requiredImports.addAll(testGenInfo.resultObjectCodeGenerator.getRequiredImports());
        testGenInfo.requiredImports = testGenInfo.requiredImports.stream().distinct().collect(Collectors.toList());
    }

    private void setRequiredHelperObjects(TestGenInfo testGenInfo) {
        testGenInfo.requiredHelperObjects = testGenInfo.argumentObjectCodeGenerators.stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        testGenInfo.requiredHelperObjects.addAll(testGenInfo.resultObjectCodeGenerator.getRequiredHelperObjects());
        testGenInfo.requiredHelperObjects = testGenInfo.requiredHelperObjects.stream().distinct().collect(Collectors.toList());
    }

    private void setObjectsInit(TestGenInfo testGenInfo) {
        testGenInfo.objectsInit = testGenInfo.argumentObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInitCode).collect(Collectors.toList());
        testGenInfo.objectsInit = testGenInfo.objectsInit.stream()
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());
    }

    private void setArgumentsInlineCode(TestGenInfo testGenInfo) {
        testGenInfo.argumentsInlineCode = testGenInfo.argumentObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInlineCode)
                .collect(Collectors.toList());
    }
}
