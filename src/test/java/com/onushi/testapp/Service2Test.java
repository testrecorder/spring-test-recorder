package com.onushi.testapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Service2Test {
    @Test
    void add() throws Exception {
        Service2 service2 = new Service2();
        assertEquals(service2.add(2, 3), 5);
    }
}
