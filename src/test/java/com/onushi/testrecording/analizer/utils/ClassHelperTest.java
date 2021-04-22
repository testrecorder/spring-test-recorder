package com.onushi.testrecording.analizer.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassHelperTest {

    @Test
    void getFullClassName() {
        assertEquals(ClassHelper.getFullClassName(1f), "java.lang.Float");
        assertEquals(ClassHelper.getFullClassName(null), "null");
    }

    @Test
    void getShortClassName() {
        assertEquals(ClassHelper.getShortClassName(1f), "Float");
        assertEquals(ClassHelper.getShortClassName(null), "null");
    }

    @Test
    void getPackageName() {
        assertEquals(ClassHelper.getPackageName(1f), "java.lang");
        assertEquals(ClassHelper.getPackageName(null), "");
    }

    @Test
    void getObjectNameBase() {
        assertEquals(ClassHelper.getObjectNameBase(1f), "float");
        assertEquals(ClassHelper.getObjectNameBase(null), "null");
    }
}
