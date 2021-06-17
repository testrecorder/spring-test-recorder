package com.onushi.springtestrecorder.analyzer.object;

import com.onushi.sample.model.StudentWithPrivateFields;
import com.onushi.sample.model.StudentWithPublicFields;
import org.junit.jupiter.api.Test;

import java.util.Map;

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
        assertEquals(FieldValueStatus.VALUE_READ, firstName.getFieldValueStatus());
        assertEquals("John", firstName.getValue());

        FieldValue lastName = objectState.get("lastName");
        assertNotNull(lastName);
        assertEquals(FieldValueStatus.VALUE_READ, lastName.getFieldValueStatus());
        assertEquals("Aris", lastName.getValue());
        assertEquals(String.class, lastName.getClazz());

        FieldValue age = objectState.get("age");
        assertNotNull(age);
        assertEquals(FieldValueStatus.VALUE_READ, age.getFieldValueStatus());
        assertEquals(30, age.getValue());
        assertEquals(int.class, age.getClazz());

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
        assertEquals(FieldValueStatus.VALUE_READ, firstName.getFieldValueStatus());

        FieldValue lastName = objectState.get("lastName");
        assertNotNull(lastName);
        assertEquals("ln", lastName.getValue());
        assertEquals(FieldValueStatus.VALUE_READ, lastName.getFieldValueStatus());
    }
}
