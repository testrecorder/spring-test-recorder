package com.onushi.testrecording.aspect;

import com.onushi.testrecording.analyzer.methodrun.MethodRunInfo;
import com.onushi.testrecording.analyzer.methodrun.MethodRunInfoBuilder;
import com.onushi.testrecording.codegenerator.test.TestGeneratorFactory;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import com.onushi.testrecording.codegenerator.test.TestGeneratorService;
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
    private final MonitorMethodSemaphore monitorMethodSemaphore;

    public RecordTestAspect(TestGeneratorFactory testGeneratorFactory,
                            TestGeneratorService testGeneratorService, MonitorMethodSemaphore monitorMethodSemaphore) {
        this.testGeneratorFactory = testGeneratorFactory;
        this.testGeneratorService = testGeneratorService;
        this.monitorMethodSemaphore = monitorMethodSemaphore;
    }

    @Around("@annotation(com.onushi.testrecording.aspect.RecordTest)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodRunInfoBuilder methodRunInfoBuilder = new MethodRunInfoBuilder();
        methodRunInfoBuilder.setMethodInvocation((MethodInvocationProceedingJoinPoint) proceedingJoinPoint);

        // TODO IB !!!! send here methodRunInfoBuilder to be added to a thread safe Set or list or something
        monitorMethodSemaphore.setMonitoring(true);
        Object result;
        Exception thrownException;
        try {
            result = proceedingJoinPoint.proceed();
            thrownException = null;
        } catch(Exception ex) {
            result = null;
            thrownException = ex;
        }
        monitorMethodSemaphore.setMonitoring(false);

        MethodRunInfo methodRunInfo = methodRunInfoBuilder
                .setResult(result)
                .setException(thrownException)
                .build();
        generateTestCode(methodRunInfo);

        if (thrownException != null) {
            throw thrownException;
        }
        return result;
    }

    private void generateTestCode(MethodRunInfo methodRunInfo) {

        try {
            TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);
            String testCode = testGeneratorService.generateTestCode(testGenerator);
            System.out.println(testCode);
        } catch (Exception ex) {
            System.out.println("@RecordTest could not record the test");
            ex.printStackTrace();
        }
    }
}
