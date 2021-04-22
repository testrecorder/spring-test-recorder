package com.onushi.testrecording;

import com.onushi.testrecording.analizer.test.MethodRunInfo;
import com.onushi.testrecording.analizer.test.MethodRunInfoFactory;
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
    private final TestGenService testGenService;

    public RecordTestAspect(MethodRunInfoFactory methodRunInfoFactory, TestGenService testGenService) {
        this.methodRunInfoFactory = methodRunInfoFactory;
        this.testGenService = testGenService;
    }

    @Around("@annotation(com.onushi.testrecording.RecordTestForThisMethod)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();

        MethodRunInfo methodRunInfo = methodRunInfoFactory.createMethodRunInfo((MethodInvocationProceedingJoinPoint)proceedingJoinPoint, result);
        String testString = testGenService.getTestString(methodRunInfo);
        System.out.println(testString);

        return result;
    }
}
