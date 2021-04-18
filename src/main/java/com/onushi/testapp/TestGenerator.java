package com.onushi.testapp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

// TODO IB !!!! this should also have tests
@Component
public class TestGenerator {

    public String generate(ProceedingJoinPoint proceedingJoinPoint, Object result) {
        MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint)proceedingJoinPoint;

        String packageAndClassName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        String packageName = packageAndClassName.substring(0, lastPointIndex);
        String className = packageAndClassName.substring(lastPointIndex + 1);

        return getTestString(result, methodInvocation, methodName, packageName, className);
    }

    private String getTestString(Object result, MethodInvocationProceedingJoinPoint methodInvocation, String methodName, String packageName, String className) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getBeginMarkerString());
        stringBuilder.append(getPackageString(packageName));
        stringBuilder.append(getImportsString());
        stringBuilder.append(getClassAndTestString(className, methodName, methodInvocation.getArgs(), result));
        stringBuilder.append(getEndMarkerString());
        return stringBuilder.toString();
    }

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(String packageName) {
        return String.format("package %s;%n%n", packageName);
    }

    private StringBuilder getImportsString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("import org.junit.jupiter.api.Test;%n%n");
        stringBuilder.append("import static org.junit.jupiter.api.Assertions.*;%n%n");
        return stringBuilder;
    }

    private StringBuilder getClassAndTestString(String className, String methodName, Object[] arguments, Object result) {
        StringBuilder stringBuilder = new StringBuilder();
        String classNameVar = className.substring(0,1).toLowerCase(Locale.ROOT) + className.substring(1);
        String argumentsText = "";
        if (arguments.length > 0) {
            argumentsText = Arrays.stream(arguments).map(Object::toString).collect(Collectors.joining(", "));
        }

        stringBuilder.append(String.format("class %sTest {%n", className));
        stringBuilder.append(String.format("    @Test%n"));
        stringBuilder.append(String.format("    void %s() throws Exception {%n", methodName));
        stringBuilder.append(String.format("        %s %s = new %s();%n", className, classNameVar, className));
        stringBuilder.append(String.format("        assertEquals(%s.%s(%s), %s);%n", classNameVar, methodName, argumentsText, result));
        stringBuilder.append(String.format("    }%n"));
        stringBuilder.append(String.format("}%n"));
        return stringBuilder;
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
