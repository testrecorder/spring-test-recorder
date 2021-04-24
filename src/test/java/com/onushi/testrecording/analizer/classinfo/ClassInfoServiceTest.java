package com.onushi.testrecording.analizer.classinfo;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.sampleclasses.Person;
import com.onushi.testrecording.sampleclasses.PersonWithBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassInfoServiceTest {

    @Test
    void getFullClassName() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertEquals(classInfoService.getFullClassName(1f), "java.lang.Float");
        assertEquals(classInfoService.getFullClassName(null), "null");
    }

    @Test
    void getShortClassName() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertEquals(classInfoService.getShortClassName(1f), "Float");
        assertEquals(classInfoService.getShortClassName(null), "null");
    }

    @Test
    void getPackageName() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertEquals(classInfoService.getPackageName(1f), "java.lang");
        assertEquals(classInfoService.getPackageName(null), "");
    }

    @Test
    void getObjectNameBase() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertEquals(classInfoService.getObjectNameBase(1f), "float");
        assertEquals(classInfoService.getObjectNameBase(null), "null");
    }

    @Test
    void isSpringComponent() {
    }

    @Test
    void canBeCreatedWithNoArgsConstructor() {
    }

    @Test
    void canBeCreatedWithSetters() {
    }

    @Test
    void canBeCreatedWithLombokBuilder() {
        ClassInfoService classInfoService = new ClassInfoService();
        assertTrue(classInfoService.canBeCreatedWithLombokBuilder(new PersonWithBuilder("f", "l", 25)));
        assertFalse(classInfoService.canBeCreatedWithLombokBuilder(new Person()));
    }
}

