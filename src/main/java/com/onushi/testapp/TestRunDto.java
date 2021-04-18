package com.onushi.testapp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class TestRunDto {
    private String packageName;
    private String className;
    private String methodName;
    private List<ObjectDto> arguments;
    private ObjectDto result;

    public static TestRunDto fromMethodInvocationAndResult(ProceedingJoinPoint proceedingJoinPoint, Object result) {
        MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint)proceedingJoinPoint;

        String packageAndClassName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        String packageName = packageAndClassName.substring(0, lastPointIndex);
        String className = packageAndClassName.substring(lastPointIndex + 1);

        List<ObjectDto> arguments = Arrays.stream(methodInvocation.getArgs()).map(ObjectDto::fromObject).collect(Collectors.toList());
        return TestRunDto.builder()
                .packageName(packageName)
                .className(className)
                .methodName(methodName)
                .arguments(arguments)
                .result(ObjectDto.fromObject(result))
                .build();
    }
}
