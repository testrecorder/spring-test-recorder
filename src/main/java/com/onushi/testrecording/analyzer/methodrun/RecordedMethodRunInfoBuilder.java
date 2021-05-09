package com.onushi.testrecording.analyzer.methodrun;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecordedMethodRunInfoBuilder {
    private MethodInvocationProceedingJoinPoint methodInvocation;
    private Object result;
    private Exception exception;
    private final List<DependencyMethodRunInfo> dependencyMethodRuns = Collections.synchronizedList(new ArrayList<>());

    public RecordedMethodRunInfoBuilder setMethodInvocation(MethodInvocationProceedingJoinPoint methodInvocation) {
        this.methodInvocation = methodInvocation;
        return this;
    }

    public RecordedMethodRunInfoBuilder setResult(Object result) {
        this.result = result;
        return this;
    }

    public RecordedMethodRunInfoBuilder setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public void addDependencyMethodRunInfo(DependencyMethodRunInfo dependencyMethodRunInfo) {
        dependencyMethodRuns.add(dependencyMethodRunInfo);
    }

    public RecordedMethodRunInfo build() {
        RecordedMethodRunInfo recordedMethodRunInfo = new RecordedMethodRunInfo();

        if (methodInvocation != null) {
            MethodSignature methodSignature = (MethodSignature) methodInvocation.getSignature();
            recordedMethodRunInfo.target = methodInvocation.getTarget();
            recordedMethodRunInfo.methodName = methodSignature.getName();
            recordedMethodRunInfo.arguments = Arrays.asList(methodInvocation.getArgs());
            recordedMethodRunInfo.fallBackResultType = methodSignature.getReturnType();
        }
        recordedMethodRunInfo.dependencyMethodRuns = dependencyMethodRuns;
        recordedMethodRunInfo.result = result;
        recordedMethodRunInfo.exception = exception;

        return recordedMethodRunInfo;
    }
}
