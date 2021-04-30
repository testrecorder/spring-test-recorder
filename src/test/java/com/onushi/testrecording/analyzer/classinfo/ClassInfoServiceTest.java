package com.onushi.testrecording.analyzer.classinfo;

import com.onushi.sampleapp.*;
import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
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
        List<Constructor<?>> publicConstructors = classInfoService.getPublicConstructors(Person.class);
        assertEquals(2, publicConstructors.size());
    }

    @Test
    void hasPublicNoArgsConstructor() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertTrue(classInfoService.hasPublicNoArgsConstructor(Person.class));
        assertFalse(classInfoService.hasPublicNoArgsConstructor(StudentWithBuilder.class));
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

