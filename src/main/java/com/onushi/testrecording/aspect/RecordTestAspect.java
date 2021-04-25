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
        Object result = proceedingJoinPoint.proceed();
        monitorMethodSemaphore.setMonitoring(false);

        MethodRunInfo methodRunInfo = methodRunInfoFactory.createMethodRunInfo((MethodInvocationProceedingJoinPoint)proceedingJoinPoint, result);
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(methodRunInfo);
        String testCode = testGeneratorService.generateTestCode(testGenerator);
        System.out.println(testCode);

        return result;
    }
}
