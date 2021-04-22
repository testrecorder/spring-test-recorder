package com.onushi.testrecording.analizer.test;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import com.onushi.testrecording.analizer.object.ObjectInfoService;
import com.onushi.testrecording.analizer.utils.ClassService;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestInfoService {
    private final ObjectInfoService objectInfoService;
    private final ClassService classService;

    public TestInfoService(ObjectInfoService objectInfoService, ClassService classService) {
        this.objectInfoService = objectInfoService;
        this.classService = classService;
    }

    public TestInfo createTestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object result) {
        TestInfo testInfo = new TestInfo();

        // TODO IB !!!! remove intermediary vars
        testInfo.packageName = classService.getPackageName(methodInvocation.getTarget());
        testInfo.shortClassName = classService.getShortClassName(methodInvocation.getTarget());

        testInfo.objectBeingTestedInfo = objectInfoService.createObjectInfo(methodInvocation.getTarget(), "testedObject");
        testInfo.methodName = methodInvocation.getSignature().getName();

        List<ObjectInfo> argumentObjectInfos = Arrays.stream(methodInvocation.getArgs())
                .map(x -> objectInfoService.createObjectInfo(x, generateObjectName(testInfo, x)))
                .collect(Collectors.toList());
        testInfo.argumentObjectInfos = argumentObjectInfos;

        ObjectInfo resultObjectInfo = objectInfoService.createObjectInfo(result, "expectedResult");
        testInfo.resultObjectInfo = resultObjectInfo;

        List<String> requiredImports = new ArrayList<>();
        requiredImports.add("org.junit.jupiter.api.Test");
        requiredImports.add("static org.junit.jupiter.api.Assertions.*");
        requiredImports.addAll(argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        requiredImports.addAll(resultObjectInfo.getRequiredImports());
        testInfo.requiredImports = requiredImports.stream().distinct().collect(Collectors.toList());

        List<String> requiredHelperObjects = argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        requiredHelperObjects.addAll(resultObjectInfo.getRequiredHelperObjects());
        testInfo.requiredHelperObjects = requiredHelperObjects.stream().distinct().collect(Collectors.toList());

        testInfo.targetObjectName = classService.getObjectNameBase(methodInvocation.getTarget());

        List<String> objectsInit = argumentObjectInfos.stream()
                .map(ObjectInfo::getInit).collect(Collectors.toList());
        objectsInit.add(resultObjectInfo.getInit());
        testInfo.objectsInit = objectsInit.stream()
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());

        testInfo.argumentsInlineCode = argumentObjectInfos.stream()
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.toList());

        return testInfo;
    }

    public String generateObjectName(TestInfo testInfo, Object object) {
        if (testInfo.getObjectNames().containsKey(object)) {
            return testInfo.getObjectNames().get(object);
        } else {
            String newObjectName = getNewObjectName(testInfo, object);
            testInfo.getObjectNames().put(object, newObjectName);
            return newObjectName;
        }
    }

    private String getNewObjectName(TestInfo testInfo, Object object) {
        String objectNameBase = classService.getObjectNameBase(object);
        int newIndex;
        if (testInfo.getLastIndexForObjectName().containsKey(objectNameBase)) {
            newIndex = testInfo.getLastIndexForObjectName().get(objectNameBase) + 1;
        } else {
            newIndex = 1;
        }
        testInfo.getLastIndexForObjectName().put(objectNameBase, newIndex);
        return objectNameBase + newIndex;
    }
}
