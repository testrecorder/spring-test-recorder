/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.aspect;

import com.onushi.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfoBuilder;
import com.onushi.springtestrecorder.codegenerator.test.TestGenerator;
import com.onushi.springtestrecorder.codegenerator.test.TestGeneratorFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
// @EnableAspectJAutoProxy(proxyTargetClass = true)
public class RecordMockForTestAspect {
    private final RecordingContext recordingContext;
    private final TestGeneratorFactory testGeneratorFactory;

    public RecordMockForTestAspect(RecordingContext recordingContext, TestGeneratorFactory testGeneratorFactory) {
        this.recordingContext = recordingContext;
        this.testGeneratorFactory = testGeneratorFactory;
    }

    @SuppressWarnings({"EmptyMethod", "unused"})
    @Pointcut("within(@com.onushi.springtestrecorder.aspect.RecordMockForTest *)")
    public void beanAnnotatedWithMonitor() {}

    @SuppressWarnings({"EmptyMethod", "unused"})
    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    @Around("publicMethod() && beanAnnotatedWithMonitor()")
    public Object applyRecordMockForTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // TODO IB !!!! better catch here
        DependencyMethodRunInfoBuilder dependencyMethodRunInfoBuilder = new DependencyMethodRunInfoBuilder();
        MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint) proceedingJoinPoint;
        dependencyMethodRunInfoBuilder.setThreadId(Thread.currentThread().getId())
                .setMethodInvocation((MethodInvocationProceedingJoinPoint) proceedingJoinPoint);

        Set<TestGenerator> testGeneratorsAtStart = new HashSet<>(recordingContext.getTestGeneratorSet());

        Object result;
        Exception thrownException;
        try {
            result = proceedingJoinPoint.proceed();
            thrownException = null;
        } catch(Exception ex) {
            result = null;
            thrownException = ex;
        }

        try {
            DependencyMethodRunInfo dependencyMethodRunInfo = dependencyMethodRunInfoBuilder
                    .setResult(result)
                    .setException(thrownException)
                    .build();

            Set<TestGenerator> testGeneratorsAtEnd = new HashSet<>(recordingContext.getTestGeneratorSet());
            Set<TestGenerator> intersection = new HashSet<>(testGeneratorsAtStart);
            // we add this DependencyMethodRunInfo only if the TestGenerator was present both before and after run
            intersection.retainAll(testGeneratorsAtEnd);
            // TODO IB could could there be a similar problem with the side effects on args of mocks?
            for (TestGenerator testGenerator : intersection) {
                if (testGenerator.getThreadId() == dependencyMethodRunInfo.getThreadId()) {
                    testGeneratorFactory.addDependencyMethodRun(testGenerator, dependencyMethodRunInfo);
                }
            }
        } catch (Exception ex) {
            System.out.println("Could not add DependencyMethodRunInfo for method " + methodInvocation.getSignature().getName());
            ex.printStackTrace();
        }

        if (thrownException != null) {
            throw thrownException;
        }
        return result;
    }
}

