package com.onushi.springtestrecorder.aspect;

import com.onushi.springtestrecorder.analyzer.methodrun.*;
import com.onushi.springtestrecorder.codegenerator.test.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

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

    // TODO IB !!!! 4 how can I make this AOP framework independent?
    @Around("@annotation(com.onushi.springtestrecorder.aspect.RecordTest)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RecordedMethodRunInfoBuilder recordedMethodRunInfoBuilder = new RecordedMethodRunInfoBuilder();
        recordedMethodRunInfoBuilder
                .setThreadId(Thread.currentThread().getId())
                .setMethodInvocation((MethodInvocationProceedingJoinPoint) proceedingJoinPoint);

        recordingContext.getMethodRunInfoBuilderSet().add(recordedMethodRunInfoBuilder);
        Object result;
        Exception thrownException;
        try {
            result = proceedingJoinPoint.proceed();
            thrownException = null;
        } catch(Exception ex) {
            result = null;
            thrownException = ex;
        } finally {
            recordingContext.getMethodRunInfoBuilderSet().remove(recordedMethodRunInfoBuilder);
        }

        RecordedMethodRunInfo recordedMethodRunInfo = recordedMethodRunInfoBuilder
                .setResult(result)
                .setException(thrownException)
                .build();

        try {
            TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);
            String testCode = testGeneratorService.generateTestCode(testGenerator);
            System.out.println(testCode);
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
