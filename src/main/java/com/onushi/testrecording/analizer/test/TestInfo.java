package com.onushi.testrecording.analizer.test;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import com.onushi.testrecording.analizer.object.ObjectInfoFactory;
import com.onushi.testrecording.analizer.utils.ClassHelper;
import lombok.Getter;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO IB Do I need to differentiate objects tested / sent as params / being the result?
// TODO IB handle exceptions being thrown

@Getter
public class TestInfo {
    private String packageName;
    private String shortClassName;
    private ObjectInfo objectBeingTestedInfo;
    private String methodName;
    private List<ObjectInfo> argumentObjectInfos;
    private List<String> requiredImports;
    private List<String> requiredHelperObjects;
    private String targetObjectName;
    private List<String> objectsInit;
    private List<String> argumentsInlineCode;
    private ObjectInfo resultObjectInfo;

    private TestInfo() {}

    public static TestInfo createTestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation,
                                             Object result,
                                             ObjectNameService objectNameService,
                                             ObjectInfoFactory objectInfoFactory
    ) {
        TestInfo testInfo = new TestInfo();

        testInfo.packageName = ClassHelper.getPackageName(methodInvocation.getTarget());
        testInfo.shortClassName = ClassHelper.getShortClassName(methodInvocation.getTarget());

        testInfo.objectBeingTestedInfo = objectInfoFactory.getObjectInfo(methodInvocation.getTarget(), "testedObject");
        testInfo.methodName = methodInvocation.getSignature().getName();

        List<ObjectInfo> argumentObjectInfos = Arrays.stream(methodInvocation.getArgs())
                .map(x -> objectInfoFactory.getObjectInfo(x, objectNameService.generateObjectName(x)))
                .collect(Collectors.toList());
        testInfo.argumentObjectInfos = argumentObjectInfos;

        ObjectInfo resultObjectInfo = objectInfoFactory.getObjectInfo(result, "expectedResult");
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

        testInfo.targetObjectName = ClassHelper.getObjectNameBase(methodInvocation.getTarget());

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
}
