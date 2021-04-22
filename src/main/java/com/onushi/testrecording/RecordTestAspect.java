package com.onushi.testrecording;

import com.onushi.testrecording.analizer.test.TestInfo;
import com.onushi.testrecording.analizer.test.TestInfoFactory;
import com.onushi.testrecording.generator.TestGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RecordTestAspect {
    private final TestInfoFactory testInfoFactory;
    private final TestGenerator testGenerator;

    public RecordTestAspect(TestInfoFactory testInfoFactory, TestGenerator testGenerator) {
        this.testInfoFactory = testInfoFactory;
        this.testGenerator = testGenerator;
    }

    @Around("@annotation(com.onushi.testrecording.RecordTestForThis)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();

        TestInfo testInfo = testInfoFactory.createTestInfo((MethodInvocationProceedingJoinPoint)proceedingJoinPoint, result);
        String testString = testGenerator.getTestString(testInfo);
        System.out.println(testString);

        return result;
    }
}
