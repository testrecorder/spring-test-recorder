package com.onushi.testapp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

// TODO IB !!!! this should be written better later
@Component
public class TestGenerator {

    // TODO IB !!!! this should be generated
    public void generate(ProceedingJoinPoint proceedingJoinPoint, Object result) {
        System.out.println();
        System.out.println("BEGIN GENERATED TEST =========");
        System.out.println();
        System.out.println("class Service2Test {");
        System.out.println("    @Test");
        System.out.println("    void add() throws Exception {");
        System.out.println("        Service2 service2 = new Service2();");
        System.out.println("        assertEquals(service2.add(2, 3), 5);");
        System.out.println("    }");
        System.out.println("}");
        System.out.println();
        System.out.println("END GENERATED TEST =========");
        System.out.println();
    }
}
