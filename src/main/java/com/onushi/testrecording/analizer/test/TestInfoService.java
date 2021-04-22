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

        testInfo.packageName = classService.getPackageName(methodInvocation.getTarget());
        testInfo.shortClassName = classService.getShortClassName(methodInvocation.getTarget());

        testInfo.objectBeingTestedInfo = objectInfoService.createObjectInfo(methodInvocation.getTarget(), "testedObject");
        testInfo.methodName = methodInvocation.getSignature().getName();

        testInfo.argumentObjectInfos = Arrays.stream(methodInvocation.getArgs())
                .map(x -> objectInfoService.createObjectInfo(x, generateObjectName(testInfo, x)))
                .collect(Collectors.toList());

        testInfo.resultObjectInfo = objectInfoService.createObjectInfo(result, "expectedResult");

        testInfo.requiredImports = new ArrayList<>();
        testInfo.requiredImports.add("org.junit.jupiter.api.Test");
        testInfo.requiredImports.add("static org.junit.jupiter.api.Assertions.*");
        testInfo.requiredImports.addAll(testInfo.argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        testInfo.requiredImports.addAll(testInfo.resultObjectInfo.getRequiredImports());
        testInfo.requiredImports = testInfo.requiredImports.stream().distinct().collect(Collectors.toList());

        testInfo.requiredHelperObjects = testInfo.argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        testInfo.requiredHelperObjects.addAll(testInfo.resultObjectInfo.getRequiredHelperObjects());
        testInfo.requiredHelperObjects = testInfo.requiredHelperObjects.stream().distinct().collect(Collectors.toList());

        testInfo.targetObjectName = classService.getObjectNameBase(methodInvocation.getTarget());

        testInfo.objectsInit = testInfo.argumentObjectInfos.stream()
                .map(ObjectInfo::getInit).collect(Collectors.toList());
        testInfo.objectsInit.add(testInfo.resultObjectInfo.getInit());
        testInfo.objectsInit = testInfo.objectsInit.stream()
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());

        testInfo.argumentsInlineCode = testInfo.argumentObjectInfos.stream()
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
