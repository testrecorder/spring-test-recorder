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

// TODO IB !!!! the code should be in a service or factory. The data in a structure.
// TODO IB !!!! This way you can inject the service and create the data objects
public class TestRunInfo {
    private final MethodInvocationProceedingJoinPoint methodInvocation;
    private final Object expectedResult;
    private final String packageName;
    private final String className;
    private final ObjectNameGeneratorImpl objectNameGenerator;

    public TestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object expectedResult) {
        this.methodInvocation = methodInvocation;
        this.expectedResult = expectedResult;
        String packageAndClassName = methodInvocation.getSignature().getDeclaringTypeName();
        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        this.packageName = packageAndClassName.substring(0, lastPointIndex);
        this.className = packageAndClassName.substring(lastPointIndex + 1);
        this.objectNameGenerator = new ObjectNameGeneratorImpl();
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
        results.addAll(this.getExpectedResult().getRequiredImports());
        return results.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getRequiredHelperObjects() {
        List<String> results = getArguments().stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        results.addAll(this.getExpectedResult().getRequiredHelperObjects());
        return results.stream().distinct().collect(Collectors.toList());
    }

    public String getClassNameVar() {
        return getClassName().substring(0,1).toLowerCase(Locale.ROOT) + getClassName().substring(1);
    }

    public List<String> getObjectsInit() {
        List<String> results = getArguments().stream()
                .map(ObjectInfo::getInit).collect(Collectors.toList());
        results.add(getExpectedResult().getInit());
        return results.stream()
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getArgumentsInlineCode() {
        return getArguments().stream()
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.toList());
    }


    public ObjectInfo getExpectedResult() {
        // TODO IB is this expectedResult or testResult. this is a smell
        return ObjectInfo.createObjectInfo(expectedResult, "expectedResult");
    }
}
