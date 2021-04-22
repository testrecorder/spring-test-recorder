package com.onushi.testapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class SimpleSampleServiceTest {
    @Test
    void minDate() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = simpleDateFormat.parse("2021-01-02 00:00:00.000");
        Date date2 = simpleDateFormat.parse("2021-02-03 00:00:00.000");
        SampleService sampleService = new SampleService();

        // Act
        Date result = sampleService.minDate(date1, date2);

        // Assert
        Date expectedResult = simpleDateFormat.parse("2021-01-02 00:00:00.000");
        assertEquals(result, expectedResult);
    }
}
