package com.onushi.testrecording.analizer.methodrun;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MethodRunInfoFactory {
    public MethodRunInfo createMethodRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object result) {
        MethodRunInfo methodRunInfo = new MethodRunInfo();

        MethodSignature methodSignature = (MethodSignature)methodInvocation.getSignature();
        methodRunInfo.target = methodInvocation.getTarget();
        methodRunInfo.methodName = methodSignature.getName();
        methodRunInfo.arguments = Arrays.asList(methodInvocation.getArgs());
        methodRunInfo.resultType = methodSignature.getReturnType();
        methodRunInfo.result = result;

        return methodRunInfo;
    }
}
