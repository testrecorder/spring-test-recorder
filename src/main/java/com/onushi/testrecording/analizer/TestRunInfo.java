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

// TODO IB this should have only getters
public class TestRunInfo {
    private final MethodInvocationProceedingJoinPoint methodInvocation;
    private final Object result;
    private final String packageName;
    private final String className;
    private final ObjectNameGenerator objectNameGenerator;
    private final ObjectInfoFactory objectInfoFactory;
    private final ObjectInfo objectBeingTestedInfo;
    private final String methodName;
    private final List<ObjectInfo> argumentObjectInfos;
    private final List<String> requiredImports;
    private final List<String> requiredHelperObjects;
    private final String classNameVar;
    private final List<String> objectsInit;
    private final List<String> argumentsInlineCode;
    private final ObjectInfo resultObjectInfo;

    public TestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation,
                       Object result,
                       ObjectNameGenerator objectNameGenerator,
                       ObjectInfoFactory objectInfoFactory
    ) {
        this.methodInvocation = methodInvocation;
        this.result = result;
        String packageAndClassName = methodInvocation.getSignature().getDeclaringTypeName();
        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        this.packageName = packageAndClassName.substring(0, lastPointIndex);
        this.className = packageAndClassName.substring(lastPointIndex + 1);
        this.objectNameGenerator = objectNameGenerator;
        this.objectInfoFactory = objectInfoFactory;
        this.objectBeingTestedInfo = objectInfoFactory.getObjectInfo(methodInvocation.getTarget(), "testedObject");
        this.methodName = methodInvocation.getSignature().getName();

        List<ObjectInfo> argumentObjectInfos = Arrays.stream(methodInvocation.getArgs())
                .map(x -> objectInfoFactory.getObjectInfo(x, this.objectNameGenerator.generateObjectName(x)))
                .collect(Collectors.toList());
        this.argumentObjectInfos = argumentObjectInfos;

        ObjectInfo resultObjectInfo = objectInfoFactory.getObjectInfo(result, "expectedResult");
        this.resultObjectInfo = resultObjectInfo;


        List<String> requiredImports = new ArrayList<>();
        requiredImports = new ArrayList<>();
        requiredImports.add("org.junit.jupiter.api.Test");
        requiredImports.add("static org.junit.jupiter.api.Assertions.*");
        requiredImports.addAll(argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        requiredImports.addAll(resultObjectInfo.getRequiredImports());
        this.requiredImports = requiredImports.stream().distinct().collect(Collectors.toList());

        List<String> requiredHelperObjects = getArgumentObjectInfos().stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        requiredHelperObjects.addAll(resultObjectInfo.getRequiredHelperObjects());
        this.requiredHelperObjects = requiredHelperObjects.stream().distinct().collect(Collectors.toList());

        this.classNameVar = getClassName().substring(0,1).toLowerCase(Locale.ROOT) + getClassName().substring(1);

        List<String> objectsInit = argumentObjectInfos.stream()
                .map(ObjectInfo::getInit).collect(Collectors.toList());
        objectsInit.add(getResultObjectInfo().getInit());
        this.objectsInit = objectsInit.stream()
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());

        this.argumentsInlineCode = argumentObjectInfos.stream()
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.toList());

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
