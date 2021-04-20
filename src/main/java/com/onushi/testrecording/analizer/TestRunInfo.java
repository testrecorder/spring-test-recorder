package com.onushi.testrecording.analizer;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

// TODO IB Do I need to differentiate objects tested / sent as params / being the result?
// TODO IB handle exceptions being thrown

public class TestRunInfo {
    private final MethodInvocationProceedingJoinPoint methodInvocation;
    private final Object testResult;
    private final String packageName;
    private final String className;
    private final ObjectNameGenerator objectNameGenerator;

    public TestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object testResult) {
        this.methodInvocation = methodInvocation;
        this.testResult = testResult;
        String packageAndClassName = methodInvocation.getSignature().getDeclaringTypeName();
        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        this.packageName = packageAndClassName.substring(0, lastPointIndex);
        this.className = packageAndClassName.substring(lastPointIndex + 1);
        objectNameGenerator = new ObjectNameGenerator();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public ObjectInfo getObjectBeingTested() {
        return ObjectInfo.createObjectInfo(methodInvocation.getTarget(), "testedObject");
    }

    public String getMethodName() {
        return methodInvocation.getSignature().getName();
    }

    public List<ObjectInfo> getArguments() {
        return Arrays.stream(methodInvocation.getArgs())
                .map(x -> ObjectInfo.createObjectInfo(x, this.objectNameGenerator.generateObjectName(x)))
                .collect(Collectors.toList());
    }

    public List<String> getRequiredImports() {
        List<String> results = new ArrayList<>();
        results.add("org.junit.jupiter.api.Test");
        results.add("static org.junit.jupiter.api.Assertions.*");
        results.addAll(this.getArguments().stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        return results.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getRequiredHelperObjects() {
        return getArguments().stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public String getClassNameVar() {
        return getClassName().substring(0,1).toLowerCase(Locale.ROOT) + getClassName().substring(1);
    }

    public List<String> getArgumentsInit() {
        return getArguments().stream()
                .map(ObjectInfo::getInit)
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getArgumentsInlineCode() {
        return getArguments().stream()
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.toList());
    }


    public ObjectInfo getTestResult() {
        return ObjectInfo.createObjectInfo(testResult, "result");
    }
}
