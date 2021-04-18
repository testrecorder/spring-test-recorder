package com.onushi.testapp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RecordTestAspect {
    private final TestGenerator testGenerator;
    public RecordTestAspect(TestGenerator testGenerator) {
        this.testGenerator = testGenerator;
    }

    // TODO IB !!!! why it doesn't work if this aspect is moved in another package?
    @Around("@annotation(com.onushi.testrecording.RecordTestForThis)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        TestRunDto testRunDto = TestRunDto.fromMethodInvocationAndResult(proceedingJoinPoint, result);
        String testString = testGenerator.getTestString(testRunDto);
        System.out.println(testString);
        return result;
    }
}
