package com.onushi.testrecording.aspect;

import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.generator.TestGenInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OtherMethodsAspect {
    @Around("execution(* com.onushi.testapp..*(..))")
    public Object applyRecordTestForThis(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        MethodInvocationProceedingJoinPoint methodInvocation =
                (MethodInvocationProceedingJoinPoint)proceedingJoinPoint;
        System.out.println("TODO IB !!!!" + methodInvocation.getSignature().getName());

//        MethodRunInfo methodRunInfo = methodRunInfoFactory.createMethodRunInfo((MethodInvocationProceedingJoinPoint)proceedingJoinPoint, result);
//        TestGenInfo testGenInfo = testGenFactory.createTestGenInfo(methodRunInfo);
//        String testString = testGenService.generateTestString(testGenInfo);
//        System.out.println(testString);

        return result;
    }
}
