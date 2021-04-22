package com.onushi.testrecording.analizer.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassServiceTest {

    @Test
    void getFullClassName() {
        ClassService classService = new ClassService();
        assertEquals(classService.getFullClassName(1f), "java.lang.Float");
        assertEquals(classService.getFullClassName(null), "null");
    }

    @Test
    void getShortClassName() {
        ClassService classService = new ClassService();
        assertEquals(classService.getShortClassName(1f), "Float");
        assertEquals(classService.getShortClassName(null), "null");
    }

    @Test
    void getPackageName() {
        ClassService classService = new ClassService();
        assertEquals(classService.getPackageName(1f), "java.lang");
        assertEquals(classService.getPackageName(null), "");
    }

    @Test
    void getObjectNameBase() {
        ClassService classService = new ClassService();
        assertEquals(classService.getObjectNameBase(1f), "float");
        assertEquals(classService.getObjectNameBase(null), "null");
    }
}
