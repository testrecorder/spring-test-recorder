package com.onushi.testrecording.aspect;

import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.analizer.methodrun.MethodRunInfoFactory;
import com.onushi.testrecording.generator.TestGenFactory;
import com.onushi.testrecording.generator.TestGenInfo;
import com.onushi.testrecording.generator.TestGenService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RecordTestAspect {
    private final MethodRunInfoFactory methodRunInfoFactory;
    private final TestGenFactory testGenFactory;
    private final TestGenService testGenService;
    private final MonitorMethodSemaphore monitorMethodSemaphore;

    public RecordTestAspect(MethodRunInfoFactory methodRunInfoFactory, TestGenFactory testGenFactory,
                            TestGenService testGenService, MonitorMethodSemaphore monitorMethodSemaphore) {
        this.methodRunInfoFactory = methodRunInfoFactory;
        this.testGenFactory = testGenFactory;
        this.testGenService = testGenService;
        this.monitorMethodSemaphore = monitorMethodSemaphore;
    }

    @Around("@annotation(com.onushi.testrecording.aspect.RecordTest)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        monitorMethodSemaphore.setMonitoring(true);
        Object result = proceedingJoinPoint.proceed();
        monitorMethodSemaphore.setMonitoring(false);

        MethodRunInfo methodRunInfo = methodRunInfoFactory.createMethodRunInfo((MethodInvocationProceedingJoinPoint)proceedingJoinPoint, result);
        TestGenInfo testGenInfo = testGenFactory.createTestGenInfo(methodRunInfo);
        String testString = testGenService.generateTestString(testGenInfo);
        System.out.println(testString);

        return result;
    }
}
