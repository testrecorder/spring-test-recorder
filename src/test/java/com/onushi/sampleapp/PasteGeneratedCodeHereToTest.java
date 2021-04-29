package com.onushi.sampleapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class SampleServiceTest {
    @Test
    void returnPerson() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SampleService sampleService = new SampleService();

        // Act
        Person result = sampleService.returnPerson();

        // Assert
        Date date1 = simpleDateFormat.parse("2021-01-01 00:00:00.000");
        Person expectedResult = Person.builder()
                .dateOfBirth(date1)
                .firstName("Gica")
                .lastName("Fulgerica")
                .build();
        assertEquals(expectedResult, result);
    }
}
