package com.onushi.testrecording;

import com.onushi.testrecording.analizer.MethodInvocationAnalyzer;
import com.onushi.testrecording.dto.TestRunDto;
import com.onushi.testrecording.generator.TestGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RecordTestAspect {
    private final MethodInvocationAnalyzer methodInvocationAnalyzer;
    private final TestGenerator testGenerator;
    public RecordTestAspect(MethodInvocationAnalyzer methodInvocationAnalyzer, TestGenerator testGenerator) {
        this.methodInvocationAnalyzer = methodInvocationAnalyzer;
        this.testGenerator = testGenerator;
    }

    @Around("@annotation(com.onushi.testrecording.RecordTestForThis)")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        TestRunDto testRunDto = methodInvocationAnalyzer.analyzeTestRun(proceedingJoinPoint, result);
        String testString = testGenerator.getTestString(testRunDto);
        System.out.println(testString);
        return result;
    }
}
