package com.onushi.testrecording.analizer.methodrun;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import com.onushi.testrecording.analizer.object.ObjectInfoService;
import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class MethodRunInfoFactory {
    private final ObjectInfoService objectInfoService;
    private final MethodRunInfoService methodRunInfoService;
    private final ClassInfoService classInfoService;

    public MethodRunInfoFactory(ObjectInfoService objectInfoService, MethodRunInfoService methodRunInfoService, ClassInfoService classInfoService) {
        this.objectInfoService = objectInfoService;
        this.methodRunInfoService = methodRunInfoService;
        this.classInfoService = classInfoService;
    }

    public MethodRunInfo createMethodRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object result) {
        MethodRunInfo methodRunInfo = new MethodRunInfo();

        methodRunInfo.packageName = classInfoService.getPackageName(methodInvocation.getTarget());
        methodRunInfo.shortClassName = classInfoService.getShortClassName(methodInvocation.getTarget());

        methodRunInfo.objectBeingTestedInfo = objectInfoService.createObjectInfo(methodInvocation.getTarget(), "testedObject");
        methodRunInfo.methodName = methodInvocation.getSignature().getName();

        setArgumentObjectInfos(methodInvocation, methodRunInfo);

        methodRunInfo.resultObjectInfo = objectInfoService.createObjectInfo(result, "expectedResult");

        setRequiredImports(methodRunInfo);

        setRequiredHelperObjects(methodRunInfo);

        methodRunInfo.targetObjectName = classInfoService.getObjectNameBase(methodInvocation.getTarget());

        setObjectsInit(methodRunInfo);

        setArgumentsInlineCode(methodRunInfo);

        return methodRunInfo;
    }

    private void setArgumentObjectInfos(MethodInvocationProceedingJoinPoint methodInvocation, MethodRunInfo methodRunInfo) {
        methodRunInfo.argumentObjectInfos = Arrays.stream(methodInvocation.getArgs())
                .map(x -> objectInfoService.createObjectInfo(x, methodRunInfoService.generateObjectName(methodRunInfo, x)))
                .collect(Collectors.toList());
    }

    private void setRequiredImports(MethodRunInfo methodRunInfo) {
        methodRunInfo.requiredImports = new ArrayList<>();
        methodRunInfo.requiredImports.add("org.junit.jupiter.api.Test");
        methodRunInfo.requiredImports.add("static org.junit.jupiter.api.Assertions.*");
        methodRunInfo.requiredImports.addAll(methodRunInfo.argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        methodRunInfo.requiredImports.addAll(methodRunInfo.resultObjectInfo.getRequiredImports());
        methodRunInfo.requiredImports = methodRunInfo.requiredImports.stream().distinct().collect(Collectors.toList());
    }

    private void setRequiredHelperObjects(MethodRunInfo methodRunInfo) {
        methodRunInfo.requiredHelperObjects = methodRunInfo.argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        methodRunInfo.requiredHelperObjects.addAll(methodRunInfo.resultObjectInfo.getRequiredHelperObjects());
        methodRunInfo.requiredHelperObjects = methodRunInfo.requiredHelperObjects.stream().distinct().collect(Collectors.toList());
    }

    private void setObjectsInit(MethodRunInfo methodRunInfo) {
        methodRunInfo.objectsInit = methodRunInfo.argumentObjectInfos.stream()
                .map(ObjectInfo::getInit).collect(Collectors.toList());
        methodRunInfo.objectsInit.add(methodRunInfo.resultObjectInfo.getInit());
        methodRunInfo.objectsInit = methodRunInfo.objectsInit.stream()
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());
    }

    private void setArgumentsInlineCode(MethodRunInfo methodRunInfo) {
        methodRunInfo.argumentsInlineCode = methodRunInfo.argumentObjectInfos.stream()
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.toList());
    }
}
