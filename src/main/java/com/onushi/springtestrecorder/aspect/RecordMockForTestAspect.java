package com.onushi.springtestrecorder.aspect;

import com.onushi.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfoBuilder;
import com.onushi.springtestrecorder.analyzer.methodrun.RecordedMethodRunInfoBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
// @EnableAspectJAutoProxy(proxyTargetClass = true)
public class RecordMockForTestAspect {
    private final RecordingContext recordingContext;

    public RecordMockForTestAspect(RecordingContext recordingContext) {
        this.recordingContext = recordingContext;
    }

    @SuppressWarnings({"EmptyMethod", "unused"})
    @Pointcut("within(@com.onushi.springtestrecorder.aspect.RecordMockForTest *)")
    public void beanAnnotatedWithMonitor() {}

    @SuppressWarnings({"EmptyMethod", "unused"})
    @Pointcut("execution(public * *(..))")
    public void publicMethod() {}

    @Around("publicMethod() && beanAnnotatedWithMonitor()")
    public Object applyRecordMockForTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        DependencyMethodRunInfoBuilder dependencyMethodRunInfoBuilder = new DependencyMethodRunInfoBuilder();
        MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint) proceedingJoinPoint;
        dependencyMethodRunInfoBuilder.setThreadId(Thread.currentThread().getId())
                .setMethodInvocation((MethodInvocationProceedingJoinPoint) proceedingJoinPoint);

        Set<RecordedMethodRunInfoBuilder> testRecordingsAtStart = new HashSet<>(recordingContext.getMethodRunInfoBuilderSet());

        Object result;
        Exception thrownException;
        try {
            result = proceedingJoinPoint.proceed();
            thrownException = null;
        } catch(Exception ex) {
            result = null;
            thrownException = ex;
        }

        try {
            DependencyMethodRunInfo dependencyMethodRunInfo = dependencyMethodRunInfoBuilder
                    .setResult(result)
                    .setException(thrownException)
                    .build();

            Set<RecordedMethodRunInfoBuilder> testRecordingsAtEnd = new HashSet<>(recordingContext.getMethodRunInfoBuilderSet());
            Set<RecordedMethodRunInfoBuilder> intersection = new HashSet<>(testRecordingsAtStart);
            // we add this DependencyMethodRunInfo only if the recording was present both before and after run
            intersection.retainAll(testRecordingsAtEnd);
            for (RecordedMethodRunInfoBuilder recordedMethodRunInfoBuilder : intersection) {
                recordedMethodRunInfoBuilder.addDependencyMethodRunInfo(dependencyMethodRunInfo);
            }
        } catch (Exception ex) {
            System.out.println("Could not add DependencyMethodRunInfo for method " + methodInvocation.getSignature().getName());
            ex.printStackTrace();
        }

        if (thrownException != null) {
            throw thrownException;
        }
        return result;
    }
}

