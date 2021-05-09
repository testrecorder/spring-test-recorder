package com.onushi.testrecording.analyzer.methodrun;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MethodRunInfoBuilder {
    private MethodInvocationProceedingJoinPoint methodInvocation;
    private Object result;
    private Exception exception;
    private final List<DependencyMethodRunInfo> dependencyMethodRuns = Collections.synchronizedList(new ArrayList<>());

    public MethodRunInfoBuilder setMethodInvocation(MethodInvocationProceedingJoinPoint methodInvocation) {
        this.methodInvocation = methodInvocation;
        return this;
    }

    public MethodRunInfoBuilder setResult(Object result) {
        this.result = result;
        return this;
    }

    public MethodRunInfoBuilder setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public void addDependencyMethodRunInfo(DependencyMethodRunInfo dependencyMethodRunInfo) {
        dependencyMethodRuns.add(dependencyMethodRunInfo);
    }

    public MethodRunInfo build() {
        MethodRunInfo methodRunInfo = new MethodRunInfo();

        if (methodInvocation != null) {
            MethodSignature methodSignature = (MethodSignature) methodInvocation.getSignature();
            methodRunInfo.target = methodInvocation.getTarget();
            methodRunInfo.methodName = methodSignature.getName();
            methodRunInfo.arguments = Arrays.asList(methodInvocation.getArgs());
            methodRunInfo.fallBackResultType = methodSignature.getReturnType();
        }
        methodRunInfo.dependencyMethodRuns = dependencyMethodRuns;
        methodRunInfo.result = result;
        methodRunInfo.exception = exception;

        return methodRunInfo;
    }
}
