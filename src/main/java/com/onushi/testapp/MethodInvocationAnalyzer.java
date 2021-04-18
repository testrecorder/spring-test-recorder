package com.onushi.testapp;

import org.aspectj.lang.ProceedingJoinPoint;

public interface MethodInvocationAnalyzer {
    TestRunDto createTestRunDto(ProceedingJoinPoint proceedingJoinPoint, Object result);
}
