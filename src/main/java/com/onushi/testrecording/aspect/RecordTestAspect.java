package com.onushi.testrecording.aspect;

import com.onushi.testrecording.analyzer.methodrun.*;
import com.onushi.testrecording.codegenerator.test.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
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

    @Around("@annotation(com.onushi.testrecording.aspect.RecordTest)")
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
        generateTestCode(recordedMethodRunInfo);

        if (thrownException != null) {
            throw thrownException;
        }
        return result;
    }

    private void generateTestCode(RecordedMethodRunInfo recordedMethodRunInfo) {

        try {
            TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);
            String testCode = testGeneratorService.generateTestCode(testGenerator);
            System.out.println(testCode);
        } catch (Exception ex) {
            System.out.println("@RecordTest could not record the test");
            ex.printStackTrace();
        }
    }
}
