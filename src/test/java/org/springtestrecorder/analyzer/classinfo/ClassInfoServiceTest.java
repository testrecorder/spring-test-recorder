/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.analyzer.classinfo;

import org.springtestrecorder.analyzer.classInfo.ClassInfoService;
import org.junit.jupiter.api.Test;
import org.sample.model.*;
import org.sample.services.PersonRepositoryImpl;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClassInfoServiceTest {
    @Test
    void isSpringComponent() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertTrue(classInfoService.isSpringComponent(PersonRepositoryImpl.class));
        assertFalse(classInfoService.isSpringComponent(Person.class));
    }

    @Test
    void getPublicConstructors() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertEquals(2, classInfoService.getAccessibleConstructors(Person.class, false).size());
        assertEquals(0, classInfoService.getAccessibleConstructors(PersonWithProtectedNoArgsConstructor.class, false).size());
        assertEquals(1, classInfoService.getAccessibleConstructors(PersonWithProtectedNoArgsConstructor.class, true).size());
    }

    @Test
    void hasAccessibleNoArgsConstructor() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertTrue(classInfoService.hasAccessibleNoArgsConstructor(Person.class, false));
        assertFalse(classInfoService.hasAccessibleNoArgsConstructor(Department.class, false));
        assertFalse(classInfoService.hasAccessibleNoArgsConstructor(StudentWithBuilder.class, false));
        assertFalse(classInfoService.hasAccessibleNoArgsConstructor(PersonWithProtectedNoArgsConstructor.class, false));
        assertTrue(classInfoService.hasAccessibleNoArgsConstructor(PersonWithProtectedNoArgsConstructor.class, true));
    }

    @Test
    void canBeCreatedWithLombokBuilder() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertTrue(classInfoService.canBeCreatedWithLombokBuilder(StudentWithBuilder.class));
        assertFalse(classInfoService.canBeCreatedWithLombokBuilder(Student.class));
    }

    @Test
    void hasEquals() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertTrue(classInfoService.hasEquals(Date.class));
        assertFalse(classInfoService.hasEquals(StudentWithPublicFields.class));
    }

    @Test
    void getLombokBuilderSetters() {
        ClassInfoService classInfoService = new ClassInfoService();
        List<Method> setters = classInfoService.getLombokBuilderSetters(StudentWithBuilder.class);
        assertEquals(3, setters.size());
        assertEquals("age", setters.get(0).getName());
        assertEquals("firstName", setters.get(1).getName());
        assertEquals("lastName", setters.get(2).getName());
    }
}
