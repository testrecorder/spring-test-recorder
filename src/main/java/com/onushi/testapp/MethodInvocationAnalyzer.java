package com.onushi.testapp;

import com.onushi.testrecording.dto.TestRunDto;
import org.aspectj.lang.ProceedingJoinPoint;

public interface MethodInvocationAnalyzer {
    TestRunDto createTestRunDto(ProceedingJoinPoint proceedingJoinPoint, Object result);
}
