package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.onushi.sampleapp.model.Person;
import java.util.List;
import java.util.Arrays;

class SampleServiceTest {
    //TODO rename the test to describe the use case
    //TODO refactor the generated code to make it easier to understand
    @Test
    void getListOfPersonList() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SampleService sampleService = new SampleService();

        // Act
        List<List<Person>> result = sampleService.getListOfPersonList();

        // Assert
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).size());
        Date date1 = simpleDateFormat.parse("1980-01-02 00:00:00.000");
        Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Paul")
                .lastName("Marculescu")
                .build();
        assertEquals(person1, result.get(0).get(0));
        Date date2 = simpleDateFormat.parse("1970-02-03 00:00:00.000");
        Person person2 = Person.builder()
                .dateOfBirth(date2)
                .firstName("Gica")
                .lastName("Fulgerica")
                .build();
        assertEquals(person2, result.get(0).get(1));
    }
}
