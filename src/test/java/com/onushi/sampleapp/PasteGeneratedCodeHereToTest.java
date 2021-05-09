package com.onushi.sampleapp;

import com.onushi.sampleapp.services.SampleService;
import org.junit.jupiter.api.Test;
import com.onushi.sampleapp.model.StudentWithDefaultInitFields;
import com.onushi.sampleapp.model.StudentWithBuilder;

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
