package com.onushi.testrecording;

import com.onushi.testrecording.analizer.TestRunInfo;
import com.onushi.testrecording.analizer.TestRunInfoFactory;
import com.onushi.testrecording.generator.TestGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RecordTestAspect {
    private final TestRunInfoFactory testRunInfoFactory;
    private final TestGenerator testGenerator;

    public RecordTestAspect(TestRunInfoFactory testRunInfoFactory, TestGenerator testGenerator) {
        this.testRunInfoFactory = testRunInfoFactory;
        this.testGenerator = testGenerator;
    }

    @Around("@annotation(com.onushi.testrecording.RecordTestForThis)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();

        TestRunInfo testRunInfo = testRunInfoFactory.getTestRunInfo((MethodInvocationProceedingJoinPoint)proceedingJoinPoint, result);
        String testString = testGenerator.getTestString(testRunInfo);
        System.out.println(testString);

        return result;
    }
}
