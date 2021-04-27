package com.onushi.testrecording.aspect;

import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.analizer.methodrun.MethodRunInfoFactory;
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
    private final MethodRunInfoFactory methodRunInfoFactory;
    private final TestGeneratorFactory testGeneratorFactory;
    private final TestGeneratorService testGeneratorService;
    private final MonitorMethodSemaphore monitorMethodSemaphore;

    public RecordTestAspect(MethodRunInfoFactory methodRunInfoFactory, TestGeneratorFactory testGeneratorFactory,
                            TestGeneratorService testGeneratorService, MonitorMethodSemaphore monitorMethodSemaphore) {
        this.methodRunInfoFactory = methodRunInfoFactory;
        this.testGeneratorFactory = testGeneratorFactory;
        this.testGeneratorService = testGeneratorService;
        this.monitorMethodSemaphore = monitorMethodSemaphore;
    }

    @Around("@annotation(com.onushi.testrecording.aspect.RecordTest)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        monitorMethodSemaphore.setMonitoring(true);
        Object result;
        try {
            result = proceedingJoinPoint.proceed();
        } catch(Exception ex) {
            monitorMethodSemaphore.setMonitoring(false);
            generateTestCode((MethodInvocationProceedingJoinPoint) proceedingJoinPoint, null, ex);
            throw ex;
        }

        monitorMethodSemaphore.setMonitoring(false);
        generateTestCode((MethodInvocationProceedingJoinPoint) proceedingJoinPoint, result, null);
        return result;
    }

    private void generateTestCode(MethodInvocationProceedingJoinPoint proceedingJoinPoint, Object result, Exception exception) throws Exception {
        MethodRunInfo methodRunInfo = methodRunInfoFactory.createMethodRunInfo(proceedingJoinPoint, result, exception);
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);
        String testCode = testGeneratorService.generateTestCode(testGenerator);
        System.out.println(testCode);
    }
}
