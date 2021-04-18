package com.onushi.testrecording.analizer;

import com.onushi.testrecording.dto.TestRunDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan
@Component
public interface MethodInvocationAnalyzer {
    TestRunDto createTestRunDto(ProceedingJoinPoint proceedingJoinPoint, Object result);
}
