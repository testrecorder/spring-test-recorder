package com.onushi.testrecording.analizer.classinfo;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.sampleclasses.Student;
import com.onushi.testrecording.sampleclasses.StudentWithBuilder;
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
        assertTrue(classInfoService.canBeCreatedWithLombokBuilder(new StudentWithBuilder("f", "l", 25)));
        assertFalse(classInfoService.canBeCreatedWithLombokBuilder(new Student()));
    }
}

