package com.onushi.testapp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

// TODO IB !!!! this should be written better later
@Component
public class TestGenerator {

    public void generate(ProceedingJoinPoint proceedingJoinPoint, Object result) {
        MethodInvocationProceedingJoinPoint methodInvocation = (MethodInvocationProceedingJoinPoint)proceedingJoinPoint;

        String packageAndClassName = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        int lastPointIndex = packageAndClassName.lastIndexOf(".");
        String packageName = packageAndClassName.substring(0, lastPointIndex);
        String className = packageAndClassName.substring(lastPointIndex + 1);

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
        System.out.println("package " + packageName + ";");
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

        // TODO IB !!!! String.format() here
        System.out.println("class " + className + "Test {");
        System.out.println("    @Test");
        System.out.println("    void " + methodName + "() throws Exception {");
        System.out.println("        " + className + " " + classNameVar + " = new " + className + "();");
        String argumentsText = "";
        if (arguments.length > 0) {
            argumentsText = Arrays.stream(arguments).map(Object::toString).collect(Collectors.joining(", "));
        }
        System.out.println("        assertEquals(" + classNameVar + "." + methodName + "(" + argumentsText + "), " + result + ");");
        System.out.println("    }");
        System.out.println("}");
    }

    private void printEndMarker() {
        System.out.println();
        System.out.println("END GENERATED TEST =========");
        System.out.println();
    }
}
