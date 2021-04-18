package com.onushi.testapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Service2Test {
    @Test
    void addPositiveNumbers() throws Exception {
        Service2 service2 = new Service2();
        assertEquals(service2.add(2, 3), 5);
    }

    @Test
    void addWithNegativeNumbers() throws Exception {
        Service2 service2 = new Service2();
        assertEquals(service2.add(-6, 4), -2);
    }
}
