package com.onushi.testrecording.aspect;

import com.onushi.testrecording.analyzer.methodrun.MethodRunInfo;
import com.onushi.testrecording.analyzer.methodrun.MethodRunInfoBuilder;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RecordMockForTestAspect {
    private final RecordingContext recordingContext;

    public RecordMockForTestAspect(RecordingContext recordingContext) {
        this.recordingContext = recordingContext;
    }

    @Pointcut("within(@com.onushi.testrecording.aspect.RecordMockForTest *)")
    public void beanAnnotatedWithMonitor() {}

    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    @Around("publicMethod() && beanAnnotatedWithMonitor()")
    public Object applyRecordMockForTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint) proceedingJoinPoint;
        if (recordingContext.getMethodRunInfoBuilderSet().size() > 0) {
            System.out.println("before call inside @RecordTest " + methodInvocation.getTarget().getClass().getName() + "." + methodInvocation.getSignature().getName());
        }
        Object result = proceedingJoinPoint.proceed();
        if (recordingContext.getMethodRunInfoBuilderSet().size() > 0) {
            System.out.println("after call inside @RecordTest " + methodInvocation.getTarget().getClass().getName() + "." + methodInvocation.getSignature().getName());
        }

        return result;
    }
}

