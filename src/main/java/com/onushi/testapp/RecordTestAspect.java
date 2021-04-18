package com.onushi.testapp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        TestRunDto testRunDto = createTestRunDto(proceedingJoinPoint, result);
        String testString = testGenerator.getTestString(testRunDto);
        System.out.println(testString);
        return result;
    }


    public TestRunDto createTestRunDto(ProceedingJoinPoint proceedingJoinPoint, Object result) {
        MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint)proceedingJoinPoint;

        String packageAndClassName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        String packageName = packageAndClassName.substring(0, lastPointIndex);
        String className = packageAndClassName.substring(lastPointIndex + 1);

        List<ObjectDto> arguments = Arrays.stream(methodInvocation.getArgs()).map(RecordTestAspect::createObjectDto).collect(Collectors.toList());
        return TestRunDto.builder()
                .packageName(packageName)
                .className(className)
                .methodName(methodName)
                .arguments(arguments)
                .result(createObjectDto(result))
                .build();
    }

    public static ObjectDto createObjectDto(Object object) {
        return ObjectDto.builder()
                .typeName(object.getClass().toString())
                .value(object.toString())
                .build();
    }
}
