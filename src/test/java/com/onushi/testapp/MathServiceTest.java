package com.onushi.testapp;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class MathServiceTest {
    @Test
    void addPositiveNumbers() throws Exception {
        MathService mathService = new MathService();
        assertEquals(mathService.add(2, 3), 5);
    }

    @Test
    void addWithNegativeNumbers() throws Exception {
        MathService mathService = new MathService();
        assertEquals(mathService.add(-6, 4), -2);
    }

    @Test
    void negate() throws Exception {
        MathService mathService = new MathService();
        assertEquals(mathService.negate(-6), 6);
    }

    @Test
    void returnZero() throws Exception {
        MathService mathService = new MathService();
        assertEquals(mathService.returnZero(), 0);
    }

    @Test
    void addFloats() throws Exception {
        MathService mathService = new MathService();
        assertEquals(mathService.addFloats(2.0f, 5.0f), 7.0f);
    }

    @Test
    void addStrings() throws Exception {
        MathService mathService = new MathService();
        assertEquals(mathService.addStrings("2", "5"), 7);
    }

    @Test
    void logicalAnd() throws Exception {
        MathService mathService = new MathService();
        assertEquals(mathService.logicalAnd(true, true), true);
    }

    @Test
    void toYYYY_MM_DD_T_HH_MM_SS_Z() throws Exception {
        MathService mathService = new MathService();
        assertEquals(mathService.toYYYY_MM_DD_T_HH_MM_SS_Z(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2021-04-18 18:55:04.385")), "2021-04-18T15:55:04Z");
    }
}
