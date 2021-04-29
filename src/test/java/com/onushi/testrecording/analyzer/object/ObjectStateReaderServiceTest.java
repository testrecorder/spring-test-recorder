package com.onushi.testrecording.analyzer.object;

import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import com.onushi.sampleapp.StudentWithBuilder;
import com.onushi.sampleapp.StudentWithPublicFields;
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

        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService(objectNameGenerator);
        Map<String, Object> objectState = objectStateReaderService.readObjectState(student);
        assertEquals("John", objectState.get("firstName"));
        assertEquals("Aris", objectState.get("lastName"));
        assertEquals(30, objectState.get("age"));
    }

    @Test
    void readObjectStateFromGetters() {
        StudentWithBuilder student = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Aris")
                .age(30)
                .build();

        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService(objectNameGenerator);
        Map<String, Object> objectState = objectStateReaderService.readObjectState(student);
        assertEquals("John", objectState.get("firstName"));
        assertEquals("Aris", objectState.get("lastName"));
        assertEquals(30, objectState.get("age"));
    }

}
