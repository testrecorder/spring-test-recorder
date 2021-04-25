package com.onushi.testrecording.analizer.classinfo;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.sampleapp.Student;
import com.onushi.sampleapp.StudentWithBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassInfoServiceTest {
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

