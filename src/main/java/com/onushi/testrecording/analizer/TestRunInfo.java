package com.onushi.testrecording.analizer;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import com.onushi.testrecording.analizer.object.ObjectInfoFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

// TODO IB Do I need to differentiate objects tested / sent as params / being the result?
// TODO IB handle exceptions being thrown

public class TestRunInfo {
    private String packageName;
    private String className;
    private ObjectInfo objectBeingTestedInfo;
    private String methodName;
    private List<ObjectInfo> argumentObjectInfos;
    private List<String> requiredImports;
    private List<String> requiredHelperObjects;
    private String classNameVar;
    private List<String> objectsInit;
    private List<String> argumentsInlineCode;
    private ObjectInfo resultObjectInfo;

    private TestRunInfo() {}

    public static TestRunInfo createTestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation,
                       Object result,
                       ObjectNameGenerator objectNameGenerator,
                       ObjectInfoFactory objectInfoFactory
    ) {
        TestRunInfo testRunInfo = new TestRunInfo();

        String packageAndClassName = methodInvocation.getSignature().getDeclaringTypeName();
        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        testRunInfo.packageName = packageAndClassName.substring(0, lastPointIndex);
        testRunInfo.className = packageAndClassName.substring(lastPointIndex + 1);

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

        testRunInfo.classNameVar = testRunInfo.className.substring(0,1).toLowerCase(Locale.ROOT) + testRunInfo.className.substring(1);

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

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public ObjectInfo getObjectBeingTestedInfo() {
        return objectBeingTestedInfo;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<ObjectInfo> getArgumentObjectInfos() {
        return argumentObjectInfos;
    }

    public List<String> getRequiredImports() {
        return requiredImports;
    }

    public List<String> getRequiredHelperObjects() {
        return requiredHelperObjects;
    }

    public String getClassNameVar() {
        return classNameVar;
    }

    public List<String> getObjectsInit() {
        return objectsInit;
    }

    public List<String> getArgumentsInlineCode() {
        return argumentsInlineCode;
    }

    public ObjectInfo getResultObjectInfo() {
        return resultObjectInfo;
    }
}
