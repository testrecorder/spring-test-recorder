package com.onushi.testrecording.analyzer.object;

import com.onushi.sampleapp.StudentWithPrivateFields;
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

        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(student);

        FieldValue firstName = objectState.get("firstName");
        assertNotNull(firstName);
        assertEquals("John", firstName.getValue());
        assertEquals(FieldValueType.VALUE_READ, firstName.getFieldValueType());

        FieldValue lastName = objectState.get("lastName");
        assertNotNull(lastName);
        assertEquals("Aris", lastName.getValue());
        assertEquals(FieldValueType.VALUE_READ, lastName.getFieldValueType());

        FieldValue age = objectState.get("age");
        assertNotNull(age);
        assertEquals(30, age.getValue());
        assertEquals(FieldValueType.VALUE_READ, age.getFieldValueType());

        assertNull(objectState.get(""));
        assertNull(objectState.get("age1"));
    }

    @Test
    void getObjectStateFromPrivateFields() {
        StudentWithPrivateFields student = new StudentWithPrivateFields("fn", "ln");

        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(student);

        FieldValue firstName = objectState.get("firstName");
        assertNotNull(firstName);
        assertEquals("fn", firstName.getValue());
        assertEquals(FieldValueType.VALUE_READ, firstName.getFieldValueType());

        FieldValue lastName = objectState.get("lastName");
        assertNotNull(lastName);
        assertEquals("ln", lastName.getValue());
        assertEquals(FieldValueType.VALUE_READ, lastName.getFieldValueType());
    }
}
