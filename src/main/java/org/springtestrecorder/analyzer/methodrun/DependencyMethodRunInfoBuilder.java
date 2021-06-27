/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.analyzer.methodrun;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.util.Arrays;

public class DependencyMethodRunInfoBuilder {
    private MethodInvocationProceedingJoinPoint methodInvocation;
    private Object result;
    private Exception exception;
    protected long threadId;

    public void setMethodInvocation(MethodInvocationProceedingJoinPoint methodInvocation) {
        this.methodInvocation = methodInvocation;
    }

    public DependencyMethodRunInfoBuilder setResult(Object result) {
        this.result = result;
        return this;
    }

    public DependencyMethodRunInfoBuilder setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public DependencyMethodRunInfoBuilder setThreadId(long threadId) {
        this.threadId = threadId;
        return this;
    }

    public DependencyMethodRunInfo build() {
        DependencyMethodRunInfo dependencyMethodRunInfo = new DependencyMethodRunInfo();

        if (methodInvocation != null) {
            MethodSignature methodSignature = (MethodSignature) methodInvocation.getSignature();
            dependencyMethodRunInfo.target = methodInvocation.getTarget();
            dependencyMethodRunInfo.methodName = methodSignature.getName();
            dependencyMethodRunInfo.arguments = Arrays.asList(methodInvocation.getArgs());
            dependencyMethodRunInfo.fallBackResultType = methodSignature.getReturnType();
        }
        dependencyMethodRunInfo.result = result;
        dependencyMethodRunInfo.exception = exception;
        dependencyMethodRunInfo.threadId = threadId;

        return dependencyMethodRunInfo;
    }
}
