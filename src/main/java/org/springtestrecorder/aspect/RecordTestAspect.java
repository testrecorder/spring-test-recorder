/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.codegenerator.test.TestGenerator;
import org.springtestrecorder.codegenerator.test.TestGeneratorFactory;
import org.springtestrecorder.codegenerator.test.TestGeneratorService;

import java.util.Arrays;

@Aspect
@Component
// @EnableAspectJAutoProxy(proxyTargetClass = true)
public class RecordTestAspect {
    private final TestGeneratorFactory testGeneratorFactory;
    private final TestGeneratorService testGeneratorService;
    private final RecordingContext recordingContext;

    public RecordTestAspect(TestGeneratorFactory testGeneratorFactory,
                            TestGeneratorService testGeneratorService, RecordingContext recordingContext) {
        this.testGeneratorFactory = testGeneratorFactory;
        this.testGeneratorService = testGeneratorService;
        this.recordingContext = recordingContext;
    }

    // TODO IB how can I make this AOP framework independent?
    @Around("@annotation(org.springtestrecorder.aspect.RecordTest)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        TestGenerator testGenerator;
        try {
            MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint) proceedingJoinPoint;
            MethodSignature methodSignature = (MethodSignature) methodInvocation.getSignature();
            testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                    .target(methodInvocation.getTarget())
                    .methodName(methodSignature.getName())
                    .arguments(Arrays.asList(methodInvocation.getArgs()))
                    .fallBackResultType(methodSignature.getReturnType())
                    .build());
        } catch(Exception ex) {
            System.out.println("@RecordTest could not record the test");
            ex.printStackTrace();
            testGenerator = null;
        }

        if (testGenerator != null) {
            recordingContext.getTestGeneratorSet().add(testGenerator);
        }
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
            if (testGenerator != null) {
                recordingContext.getTestGeneratorSet().remove(testGenerator);

                testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                        .result(result)
                        .exception(thrownException)
                        .build());

                String testCode = testGeneratorService.generateTestCode(testGenerator);
                System.out.println(testCode);
            }
        } catch (Exception ex) {
            System.out.println("@RecordTest could not record the test");
            ex.printStackTrace();
        }

        if (thrownException != null) {
            throw thrownException;
        }
        return result;
    }

}
