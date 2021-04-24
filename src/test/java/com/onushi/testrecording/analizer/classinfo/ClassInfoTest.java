package com.onushi.testrecording.analizer.classinfo;

import com.onushi.testrecording.analizer.classInfo.ClassInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ClassInfoTest {
    @Test
    void isSpringComponent() {
    }

    @Test
    void canBeCreatedWithNoArgsConstructor() {
    }

    @Test
    void canBeCreatedWithLombokBuilder() throws ClassNotFoundException {
        ClassInfo classInfo = new ClassInfo(Class.forName("com.onushi.testrecording.sampleclasses.PersonWithBuilder"));
        assertTrue(classInfo.canBeCreatedWithLombokBuilder());
        classInfo = new ClassInfo(Class.forName("com.onushi.testrecording.sampleclasses.Person"));
        assertFalse(classInfo.canBeCreatedWithLombokBuilder());
    }

    @Test
    void canBeCreatedWithSetters() {
    }
}
