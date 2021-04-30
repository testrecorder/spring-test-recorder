package com.onushi.testrecording.analyzer.object;

import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import com.onushi.sampleapp.StudentWithBuilder;
import com.onushi.sampleapp.StudentWithPublicFields;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ObjectStateReaderServiceTest {

    @Test
    void getObjectStateFromPublicFields() {
        StudentWithPublicFields student = new StudentWithPublicFields();
        student.firstName = "John";
        student.lastName = "Aris";
        student.age = 30;

        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService(objectNameGenerator);
        Map<String, Optional<Object>> objectState = objectStateReaderService.getObjectState(student);
        assertNotNull(objectState.get("firstName"));
        assertNotNull(objectState.get("lastName"));
        assertNotNull(objectState.get("age"));
        assertNull(objectState.get(""));
        assertNull(objectState.get("age1"));
        assertEquals("John", objectState.get("firstName").orElse(null));
        assertEquals("Aris", objectState.get("lastName").orElse(null));
        assertEquals(30, objectState.get("age").orElse(null));
    }

    @Test
    void getObjectStateFromGetters() {
        StudentWithBuilder student = StudentWithBuilder.builder()
                .firstName("John")
                .lastName(null)
                .age(30)
                .build();

        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService(objectNameGenerator);
        Map<String, Optional<Object>> objectState = objectStateReaderService.getObjectState(student);
        assertNotNull(objectState.get("firstName"));
        assertNotNull(objectState.get("lastName"));
        assertNotNull(objectState.get("age"));
        assertNull(objectState.get("age1"));
        assertEquals("John", objectState.get("firstName").orElse(null));
        assertNull(objectState.get("lastName").orElse(null));
        assertEquals(30, objectState.get("age").orElse(null));
    }

}
