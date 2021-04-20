package com.onushi.testrecording.analizer;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO IB Do I need to differentiate objects tested / sent as params / being the result?
// TODO IB handle exceptions being thrown

public class TestRunInfo {
    private final MethodInvocationProceedingJoinPoint methodInvocation;
    private final Object testResult;
    private final String packageName;
    private final String className;

    public TestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object testResult) {
        this.methodInvocation = methodInvocation;
        this.testResult = testResult;
        String packageAndClassName = methodInvocation.getSignature().getDeclaringTypeName();
        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        this.packageName = packageAndClassName.substring(0, lastPointIndex);
        this.className = packageAndClassName.substring(lastPointIndex + 1);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public ObjectInfo getObjectBeingTested() {
        return ObjectInfo.createObjectInfo(methodInvocation.getTarget());
    }

    public String getMethodName() {
        return methodInvocation.getSignature().getName();
    }

    public List<ObjectInfo> getArguments() {
        return Arrays.stream(methodInvocation.getArgs()).map(ObjectInfo::createObjectInfo).collect(Collectors.toList());
    }

    public ObjectInfo getTestResult() {
        return ObjectInfo.createObjectInfo(testResult);
    }
}