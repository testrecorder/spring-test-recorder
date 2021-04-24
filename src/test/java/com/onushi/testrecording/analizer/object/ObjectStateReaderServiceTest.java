package com.onushi.testrecording.analizer.object;

import com.onushi.testrecording.sampleclasses.StudentWithBuilder;
import com.onushi.testrecording.sampleclasses.StudentWithPublicFields;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectStateReaderServiceTest {

    @Test
    void readObjectStateFromPublicFields() {
        StudentWithPublicFields student = new StudentWithPublicFields();
        student.firstName = "John";
        student.lastName = "Aris";
        student.age = 30;

        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        Map<String, Object> objectState = objectStateReaderService.readObjectState(student);
        assertEquals(objectState.get("firstName"), "John");
        assertEquals(objectState.get("lastName"), "Aris");
        assertEquals(objectState.get("age"), 30);
    }

    @Test
    void readObjectStateFromGetters() {
        StudentWithBuilder student = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Aris")
                .age(30)
                .build();

        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        Map<String, Object> objectState = objectStateReaderService.readObjectState(student);
        assertEquals(objectState.get("firstName"), "John");
        assertEquals(objectState.get("lastName"), "Aris");
        assertEquals(objectState.get("age"), 30);
    }

}
