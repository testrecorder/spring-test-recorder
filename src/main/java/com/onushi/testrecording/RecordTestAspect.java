package com.onushi.testrecording;

import com.onushi.testrecording.analizer.test.TestInfo;
import com.onushi.testrecording.analizer.test.TestInfoService;
import com.onushi.testrecording.generator.TestGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RecordTestAspect {
    private final TestInfoService testInfoService;
    private final TestGenerator testGenerator;

    public RecordTestAspect(TestInfoService testInfoService, TestGenerator testGenerator) {
        this.testInfoService = testInfoService;
        this.testGenerator = testGenerator;
    }

    @Around("@annotation(com.onushi.testrecording.RecordTestForThis)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();

        TestInfo testInfo = testInfoService.createTestRunInfo((MethodInvocationProceedingJoinPoint)proceedingJoinPoint, result);
        String testString = testGenerator.getTestString(testInfo);
        System.out.println(testString);

        return result;
    }
}
