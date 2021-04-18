package com.onushi.testrecording.analizer;

import com.onushi.testrecording.dto.ObjectDto;
import com.onushi.testrecording.dto.ObjectDtoConverter;
import com.onushi.testrecording.dto.TestRunDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MethodInvocationAnalyzerImpl implements MethodInvocationAnalyzer {
    private final ObjectDtoConverter objectDtoConverter;
    public MethodInvocationAnalyzerImpl(ObjectDtoConverter objectDtoConverter) {
        this.objectDtoConverter = objectDtoConverter;
    }

    @Override
    public TestRunDto createTestRunDto(ProceedingJoinPoint proceedingJoinPoint, Object result) {
        MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint)proceedingJoinPoint;

        String packageAndClassName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        String packageName = packageAndClassName.substring(0, lastPointIndex);
        String className = packageAndClassName.substring(lastPointIndex + 1);

        List<ObjectDto> arguments = Arrays.stream(methodInvocation.getArgs()).map(objectDtoConverter::createObjectDto).collect(Collectors.toList());
        return TestRunDto.builder()
                .packageName(packageName)
                .className(className)
                .methodName(methodName)
                .arguments(arguments)
                .result(objectDtoConverter.createObjectDto(result))
                .build();
    }
}
