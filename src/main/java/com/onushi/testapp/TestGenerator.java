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

    public void generate(ProceedingJoinPoint proceedingJoinPoint, Object result) {
        MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint)proceedingJoinPoint;

        String packageAndClassName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        String packageName = packageAndClassName.substring(0, lastPointIndex);
        String className = packageAndClassName.substring(lastPointIndex + 1);

        printTest(result, methodInvocation, methodName, packageName, className);
    }

    private void printTest(Object result, MethodInvocationProceedingJoinPoint methodInvocation, String methodName, String packageName, String className) {
        printBeginMarker();

        printPackage(packageName);

        printImports();

        printClassAndTest(className, methodName, methodInvocation.getArgs(), result);

        printEndMarker();
    }

    private void printBeginMarker() {
        System.out.println();
        System.out.println("BEGIN GENERATED TEST =========");
        System.out.println();
    }

    private void printPackage(String packageName) {
        System.out.printf("package %s;%n", packageName);
        System.out.println();
    }

    private void printImports() {
        System.out.println("import org.junit.jupiter.api.Test;");
        System.out.println();
        System.out.println("import static org.junit.jupiter.api.Assertions.*;");
        System.out.println();
    }

    private void printClassAndTest(String className, String methodName, Object[] arguments, Object result) {
        String classNameVar = className.substring(0,1).toLowerCase(Locale.ROOT) + className.substring(1);
        String argumentsText = "";
        if (arguments.length > 0) {
            argumentsText = Arrays.stream(arguments).map(Object::toString).collect(Collectors.joining(", "));
        }

        System.out.printf("class %sTest {%n", className);
        System.out.println("    @Test");
        System.out.printf("    void %s() throws Exception {%n", methodName);
        System.out.printf("        %s %s = new %s();%n", className, classNameVar, className);
        System.out.printf("        assertEquals(%s.%s(%s), %s);%n", classNameVar, methodName, argumentsText, result);
        System.out.println("    }");
        System.out.println("}");
    }

    private void printEndMarker() {
        System.out.println();
        System.out.println("END GENERATED TEST =========");
        System.out.println();
    }
}
