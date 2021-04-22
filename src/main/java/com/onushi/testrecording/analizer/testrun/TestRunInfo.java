package com.onushi.testrecording.analizer.testrun;

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
public class TestRunInfo {
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

    private TestRunInfo() {}

    // TODO IB from testRun I should create a new object called generatedTest. objectNameGenerator are part of that object
    public static TestRunInfo createTestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation,
                       Object result,
                       ObjectNameGenerator objectNameGenerator,
                       ObjectInfoFactory objectInfoFactory
    ) {
        TestRunInfo testRunInfo = new TestRunInfo();

        testRunInfo.packageName = ClassHelper.getPackageName(methodInvocation.getTarget());
        testRunInfo.shortClassName = ClassHelper.getShortClassName(methodInvocation.getTarget());

        testRunInfo.objectBeingTestedInfo = objectInfoFactory.getObjectInfo(methodInvocation.getTarget(), "testedObject");
        testRunInfo.methodName = methodInvocation.getSignature().getName();

        List<ObjectInfo> argumentObjectInfos = Arrays.stream(methodInvocation.getArgs())
                .map(x -> objectInfoFactory.getObjectInfo(x, objectNameGenerator.generateObjectName(x)))
                .collect(Collectors.toList());
        testRunInfo.argumentObjectInfos = argumentObjectInfos;

        ObjectInfo resultObjectInfo = objectInfoFactory.getObjectInfo(result, "expectedResult");
        testRunInfo.resultObjectInfo = resultObjectInfo;

        List<String> requiredImports = new ArrayList<>();
        requiredImports.add("org.junit.jupiter.api.Test");
        requiredImports.add("static org.junit.jupiter.api.Assertions.*");
        requiredImports.addAll(argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        requiredImports.addAll(resultObjectInfo.getRequiredImports());
        testRunInfo.requiredImports = requiredImports.stream().distinct().collect(Collectors.toList());

        List<String> requiredHelperObjects = argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        requiredHelperObjects.addAll(resultObjectInfo.getRequiredHelperObjects());
        testRunInfo.requiredHelperObjects = requiredHelperObjects.stream().distinct().collect(Collectors.toList());

        testRunInfo.targetObjectName = ClassHelper.getObjectNameBase(methodInvocation.getTarget());

        List<String> objectsInit = argumentObjectInfos.stream()
                .map(ObjectInfo::getInit).collect(Collectors.toList());
        objectsInit.add(resultObjectInfo.getInit());
        testRunInfo.objectsInit = objectsInit.stream()
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());

        testRunInfo.argumentsInlineCode = argumentObjectInfos.stream()
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.toList());

        return testRunInfo;
    }
}
