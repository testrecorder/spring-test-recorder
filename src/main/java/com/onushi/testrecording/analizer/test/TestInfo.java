package com.onushi.testrecording.analizer.test;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import com.onushi.testrecording.analizer.object.ObjectInfoFactory;
import com.onushi.testrecording.analizer.utils.ClassService;
import lombok.Getter;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.util.*;
import java.util.stream.Collectors;

// TODO IB Do I need to differentiate objects tested / sent as params / being the result?
// TODO IB handle exceptions being thrown

// TODO IB !!!! maybe these are 2 objects after all
@Getter
public class TestInfo {
    protected String packageName;
    protected String shortClassName;
    protected ObjectInfo objectBeingTestedInfo;
    protected String methodName;
    protected List<ObjectInfo> argumentObjectInfos;
    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;
    protected String targetObjectName;
    protected List<String> objectsInit;
    protected List<String> argumentsInlineCode;
    protected ObjectInfo resultObjectInfo;
    protected final Map<Object, String> objectNames = new HashMap<>();
    protected final Map<String, Integer> lastIndexForObjectName = new HashMap<>();

    private TestInfo() {}

    public static TestInfo createTestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation,
                                             Object result,
                                             ClassService classService,
                                             ObjectNameService objectNameService,
                                             ObjectInfoFactory objectInfoFactory
    ) {
        TestInfo testInfo = new TestInfo();

        testInfo.packageName = classService.getPackageName(methodInvocation.getTarget());
        testInfo.shortClassName = classService.getShortClassName(methodInvocation.getTarget());

        testInfo.objectBeingTestedInfo = objectInfoFactory.getObjectInfo(methodInvocation.getTarget(), "testedObject");
        testInfo.methodName = methodInvocation.getSignature().getName();

        List<ObjectInfo> argumentObjectInfos = Arrays.stream(methodInvocation.getArgs())
                .map(x -> objectInfoFactory.getObjectInfo(x, objectNameService.generateObjectName(testInfo, x)))
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
}
