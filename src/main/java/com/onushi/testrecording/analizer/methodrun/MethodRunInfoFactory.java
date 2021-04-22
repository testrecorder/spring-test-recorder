package com.onushi.testrecording.analizer.methodrun;

import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MethodRunInfoFactory {
    public MethodRunInfo createMethodRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object result) {
        MethodRunInfo methodRunInfo = new MethodRunInfo();

        methodRunInfo.target = methodInvocation.getTarget();
        methodRunInfo.methodName = methodInvocation.getSignature().getName();
        methodRunInfo.arguments = Arrays.asList(methodInvocation.getArgs());
        methodRunInfo.result = result;

        return methodRunInfo;
    }
}
