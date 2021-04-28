package com.onushi.sampleapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;;
import java.util.List;
import java.util.Arrays;
import com.onushi.sampleapp.StudentWithBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;

class SampleServiceTest {
    @Test
    void someFunction() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        List<String> arrayList1 =  Arrays.asList("Maldive", "Tenerife", "Bahamas");
        StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()
                .age(35)
                .firstName("Paul")
                .lastName("Marculescu")
                .build();
        int[] array1 = {1, 2, 3};
        SampleService sampleService = new SampleService();

        // Act
        Date result = sampleService.someFunction(arrayList1, studentWithBuilder1, array1);

        // Assert
        Date expectedResult = simpleDateFormat.parse("2022-01-01 00:00:00.000");
        assertEquals(expectedResult, result);
    }
}
