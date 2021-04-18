package com.onushi.testapp;

import org.junit.jupiter.api.Test;

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
}
