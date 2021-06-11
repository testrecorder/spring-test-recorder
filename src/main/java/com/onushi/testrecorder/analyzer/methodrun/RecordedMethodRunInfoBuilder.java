package com.onushi.testrecorder.analyzer.methodrun;

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
    private long threadId;

    public void setMethodInvocation(MethodInvocationProceedingJoinPoint methodInvocation) {
        this.methodInvocation = methodInvocation;
    }

    public RecordedMethodRunInfoBuilder setResult(Object result) {
        this.result = result;
        return this;
    }

    public RecordedMethodRunInfoBuilder setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public RecordedMethodRunInfoBuilder setThreadId(long threadId) {
        this.threadId = threadId;
        return this;
    }

    public void addDependencyMethodRunInfo(DependencyMethodRunInfo dependencyMethodRunInfo) {
        // TODO IB LATER this check should be optional
        if (dependencyMethodRunInfo.threadId == threadId) {
            dependencyMethodRuns.add(dependencyMethodRunInfo);
        }
    }

    public RecordedMethodRunInfo build() {
        RecordedMethodRunInfo recordedMethodRunInfo = new RecordedMethodRunInfo();

        if (methodInvocation != null) {
            MethodSignature methodSignature = (MethodSignature) methodInvocation.getSignature();
            recordedMethodRunInfo.methodName = methodSignature.getName();
            recordedMethodRunInfo.target = methodInvocation.getTarget();
            recordedMethodRunInfo.arguments = Arrays.asList(methodInvocation.getArgs());
            recordedMethodRunInfo.fallBackResultType = methodSignature.getReturnType();
        }
        recordedMethodRunInfo.dependencyMethodRuns = dependencyMethodRuns;
        recordedMethodRunInfo.result = result;
        recordedMethodRunInfo.exception = exception;
        recordedMethodRunInfo.threadId = threadId;

        return recordedMethodRunInfo;
    }
}
