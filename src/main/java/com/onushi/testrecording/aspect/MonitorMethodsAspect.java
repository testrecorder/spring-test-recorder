package com.onushi.testrecording.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;


@Aspect
@Component
// This is used to monitor method calls inside @RecordTest method
// This will not catch calls inside the same class with the @RecordTest which is ok
public class MonitorMethodsAspect {
    private final MonitorMethodSemaphore monitorMethodSemaphore;

    public MonitorMethodsAspect(MonitorMethodSemaphore monitorMethodSemaphore) {
        this.monitorMethodSemaphore = monitorMethodSemaphore;
    }

    @Around("execution(* com.onushi.sampleapp..*(..))")
    public Object monitorMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodInvocationProceedingJoinPoint methodInvocation =
                (MethodInvocationProceedingJoinPoint) proceedingJoinPoint;
        if (monitorMethodSemaphore.isMonitoring()) {
            System.out.println("before call inside @RecordTest " + methodInvocation.getSignature().getName());
        }
        Object result = proceedingJoinPoint.proceed();
        if (monitorMethodSemaphore.isMonitoring()) {
            System.out.println("after call inside @RecordTest " + methodInvocation.getSignature().getName());
        }

//        MethodRunInfo methodRunInfo = methodRunInfoFactory.createMethodRunInfo((MethodInvocationProceedingJoinPoint)proceedingJoinPoint, result);
//        TestGenInfo testGenInfo = testGenFactory.createTestGenInfo(methodRunInfo);
//        String testString = testGenService.generateTestString(testGenInfo);
//        System.out.println(testString);

        return result;
    }
}
