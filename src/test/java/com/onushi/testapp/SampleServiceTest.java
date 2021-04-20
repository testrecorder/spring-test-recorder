package com.onushi.testapp;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SampleServiceTest {
    @Test
    void addPositiveNumbers() throws Exception {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.add(2, 3), 5);
    }

    @Test
    void addWithNegativeNumbers() throws Exception {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.add(-6, 4), -2);
    }

    @Test
    void negate() throws Exception {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.negate(-6), 6);
    }

    @Test
    void returnZero() throws Exception {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.returnZero(), 0);
    }

    @Test
    void addFloats() throws Exception {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.addFloats(2.0f, 3.0f), 5.0f);
    }

    @Test
    void addStrings() throws Exception {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.addStrings("2", "5"), 7);
    }

    @Test
    void logicalAnd() throws Exception {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.logicalAnd(true, true), true);
    }

    @Test
    void toYYYY_MM_DD_T_HH_MM_SS_Z() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = simpleDateFormat.parse("2021-04-20 17:02:37.437");
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.toYYYY_MM_DD_T_HH_MM_SS_Z(date1, date1), "2021-04-20T14:02:37Z");
    }

    @Test
    void testTypes() throws Exception {
        SampleService sampleService = new SampleService();
        assertEquals(sampleService.testTypes((short)6, (byte)4, 5, true, 'c', 1.5), 5);
    }
}
