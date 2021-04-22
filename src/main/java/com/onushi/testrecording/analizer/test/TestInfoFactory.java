package com.onushi.testrecording.analizer.test;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import com.onushi.testrecording.analizer.object.ObjectInfoService;
import com.onushi.testrecording.analizer.utils.ClassService;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class TestInfoFactory {
    private final ObjectInfoService objectInfoService;
    private final TestInfoService testInfoService;
    private final ClassService classService;

    public TestInfoFactory(ObjectInfoService objectInfoService, TestInfoService testInfoService, ClassService classService) {
        this.objectInfoService = objectInfoService;
        this.testInfoService = testInfoService;
        this.classService = classService;
    }

    public TestInfo createTestInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object result) {
        TestInfo testInfo = new TestInfo();

        testInfo.packageName = classService.getPackageName(methodInvocation.getTarget());
        testInfo.shortClassName = classService.getShortClassName(methodInvocation.getTarget());

        testInfo.objectBeingTestedInfo = objectInfoService.createObjectInfo(methodInvocation.getTarget(), "testedObject");
        testInfo.methodName = methodInvocation.getSignature().getName();

        setArgumentObjectInfos(methodInvocation, testInfo);

        testInfo.resultObjectInfo = objectInfoService.createObjectInfo(result, "expectedResult");

        setRequiredImports(testInfo);

        setRequiredHelperObjects(testInfo);

        testInfo.targetObjectName = classService.getObjectNameBase(methodInvocation.getTarget());

        setObjectsInit(testInfo);

        setArgumentsInlineCode(testInfo);

        return testInfo;
    }

    private void setArgumentObjectInfos(MethodInvocationProceedingJoinPoint methodInvocation, TestInfo testInfo) {
        testInfo.argumentObjectInfos = Arrays.stream(methodInvocation.getArgs())
                .map(x -> objectInfoService.createObjectInfo(x, testInfoService.generateObjectName(testInfo, x)))
                .collect(Collectors.toList());
    }

    private void setRequiredImports(TestInfo testInfo) {
        testInfo.requiredImports = new ArrayList<>();
        testInfo.requiredImports.add("org.junit.jupiter.api.Test");
        testInfo.requiredImports.add("static org.junit.jupiter.api.Assertions.*");
        testInfo.requiredImports.addAll(testInfo.argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        testInfo.requiredImports.addAll(testInfo.resultObjectInfo.getRequiredImports());
        testInfo.requiredImports = testInfo.requiredImports.stream().distinct().collect(Collectors.toList());
    }

    private void setRequiredHelperObjects(TestInfo testInfo) {
        testInfo.requiredHelperObjects = testInfo.argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        testInfo.requiredHelperObjects.addAll(testInfo.resultObjectInfo.getRequiredHelperObjects());
        testInfo.requiredHelperObjects = testInfo.requiredHelperObjects.stream().distinct().collect(Collectors.toList());
    }

    private void setObjectsInit(TestInfo testInfo) {
        testInfo.objectsInit = testInfo.argumentObjectInfos.stream()
                .map(ObjectInfo::getInit).collect(Collectors.toList());
        testInfo.objectsInit.add(testInfo.resultObjectInfo.getInit());
        testInfo.objectsInit = testInfo.objectsInit.stream()
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());
    }

    private void setArgumentsInlineCode(TestInfo testInfo) {
        testInfo.argumentsInlineCode = testInfo.argumentObjectInfos.stream()
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.toList());
    }
}
