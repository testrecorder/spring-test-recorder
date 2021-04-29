package com.onushi.sampleapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;;
import java.util.List;
import java.util.Arrays;
import com.onushi.sampleapp.Person;

class SampleServiceTest {
    @Test
    void someFunction() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = simpleDateFormat.parse("1980-01-02 00:00:00.000");
        Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Paul")
                .lastName("Marculescu")
                .build();
        Date date2 = simpleDateFormat.parse("1970-02-03 00:00:00.000");
        Person person2 = Person.builder()
                .dateOfBirth(date2)
                .firstName("Gica")
                .lastName("Fulgerica")
                .build();
        List<Person> arrayList1 =  Arrays.asList(person1, person2);
        Person[] array1 = {person1, person2};
        SampleService sampleService = new SampleService();

        // Act
        List result = sampleService.someFunction(arrayList1, array1);

        // Assert
        List<Person> expectedResult =  Arrays.asList(person1, person2);
        assertEquals(expectedResult, result);
    }
}
