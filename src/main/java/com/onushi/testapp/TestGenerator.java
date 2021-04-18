package com.onushi.testapp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

// TODO IB !!!! the generators should receive an DTO with DTOs
@Component
public class TestGenerator {

    // TODO IB this should also have tests
    // TODO IB in the final version I should group tests for the same class together
    public String getTestString(TestRunDto testRunDto) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getBeginMarkerString());
        stringBuilder.append(getPackageString(testRunDto.getPackageName()));
        stringBuilder.append(getImportsString());
        stringBuilder.append(getClassAndTestString(testRunDto.getClassName(), testRunDto.getMethodName(), testRunDto.getArguments(), testRunDto.getResult()));
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
        stringBuilder.append(String.format("import org.junit.jupiter.api.Test;%n%n"));
        stringBuilder.append(String.format("import static org.junit.jupiter.api.Assertions.*;%n%n"));
        return stringBuilder;
    }

    private StringBuilder getClassAndTestString(String className, String methodName, List<ObjectDto> arguments, ObjectDto result) {
        StringBuilder stringBuilder = new StringBuilder();
        String classNameVar = className.substring(0,1).toLowerCase(Locale.ROOT) + className.substring(1);
        String argumentsText = "";
        if (arguments.size() > 0) {
            argumentsText = arguments.stream().map(ObjectDto::getValue).collect(Collectors.joining(", "));
        }

        stringBuilder.append(String.format("class %sTest {%n", className));
        stringBuilder.append(String.format("    @Test%n"));
        stringBuilder.append(String.format("    void %s() throws Exception {%n", methodName));
        stringBuilder.append(String.format("        %s %s = new %s();%n", className, classNameVar, className));
        stringBuilder.append(String.format("        assertEquals(%s.%s(%s), %s);%n", classNameVar, methodName, argumentsText, result.getValue()));
        stringBuilder.append(String.format("    }%n"));
        stringBuilder.append(String.format("}%n"));
        return stringBuilder;
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
