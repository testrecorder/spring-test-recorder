package com.onushi.testrecording.analizer.classinfo;

import com.onushi.testrecording.analizer.classInfo.ClassNameService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassNameServiceTest {

    @Test
    void getFullClassName() {
        ClassNameService classNameService = new ClassNameService();
        assertEquals(classNameService.getFullClassName(1f), "java.lang.Float");
        assertEquals(classNameService.getFullClassName(null), "null");
    }

    @Test
    void getShortClassName() {
        ClassNameService classNameService = new ClassNameService();
        assertEquals(classNameService.getShortClassName(1f), "Float");
        assertEquals(classNameService.getShortClassName(null), "null");
    }

    @Test
    void getPackageName() {
        ClassNameService classNameService = new ClassNameService();
        assertEquals(classNameService.getPackageName(1f), "java.lang");
        assertEquals(classNameService.getPackageName(null), "");
    }

    @Test
    void getObjectNameBase() {
        ClassNameService classNameService = new ClassNameService();
        assertEquals(classNameService.getObjectNameBase(1f), "float");
        assertEquals(classNameService.getObjectNameBase(null), "null");
    }
}
