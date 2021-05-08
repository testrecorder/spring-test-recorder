package com.onushi.sampleapp;

import org.junit.jupiter.api.Test;
import com.onushi.sampleapp.StudentWithDefaultInitFields;
import com.onushi.sampleapp.StudentWithBuilder;

class SampleServiceTest {
    @Test
    void processStudents() throws Exception {
        // Arrange
        StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()
                .age(60)
                .firstName("John")
                .lastName("Wayne")
                .build();
        SampleService sampleService = new SampleService();

        // Act
        sampleService.processStudents(new StudentWithDefaultInitFields(), studentWithBuilder1);

    }
}
