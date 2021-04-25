package com.onushi.sampleapp;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

// TODO IB !!!! what to do with these?
class SampleServiceTest {
    @Test
    void addPositiveNumbers() {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.add(2, 3), 5);
    }

    @Test
    void addWithNegativeNumbers() {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.add(-6, 4), -2);
    }

    @Test
    void negate() {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.negate(-6), 6);
    }

    @Test
    void returnZero() {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.returnZero(), 0);
    }

    @Test
    void addFloats() {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.addFloats(2.0f, 3.0f), 5.0f);
    }

    @Test
    void addStrings() {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.addStrings("2", "5"), 7);
    }

    @Test
    void logicalAnd() {
        SampleService sampleService = new SampleService();
        assertTrue(sampleService.logicalAnd(true, true));
    }

    @Test
    void minDate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = simpleDateFormat.parse("2021-01-02 00:00:00.000");
        Date date2 = simpleDateFormat.parse("2021-02-03 00:00:00.000");
        Date expectedResult = simpleDateFormat.parse("2021-01-02 00:00:00.000");
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.minDate(date1, date2), expectedResult);
    }

    @Test
    void testTypes() {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.testTypes((short)6, (byte)4, 5, true, 'c', 1.5), 5);
    }
}
