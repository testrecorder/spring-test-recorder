package com.onushi.sample.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import com.onushi.sample.model.Person;
import java.util.Date;
import java.text.SimpleDateFormat;

class SampleServiceTest {
    //TODO rename the test to describe the use case
    //TODO refactor the generated code to make it easier to understand
    @Test
    void createPersonHashSet() throws Exception {
        // Arrange
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SampleService sampleService = new SampleService();

        // Act
        Set<Person> result = sampleService.createPersonHashSet();

        // Assert
        assertEquals(2, result.size());
        Date date1 = simpleDateFormat.parse("1980-01-02 00:00:00.000");
        Person person1 = Person.builder()
                .dateOfBirth(date1)
                .firstName("Marco")
                .lastName("Polo")
                .build();
        assertTrue(result.contains(person1));
        Date date2 = simpleDateFormat.parse("1970-02-03 00:00:00.000");
        Person person2 = Person.builder()
                .dateOfBirth(date2)
                .firstName("Tom")
                .lastName("Richardson")
                .build();
        assertTrue(result.contains(person2));
    }
}
