/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.analyzer.object;

import org.junit.jupiter.api.Test;
import org.sample.model.StudentWithPrivateFields;
import org.sample.model.StudentWithPublicFields;

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
