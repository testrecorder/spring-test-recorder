package com.onushi.testrecording.analyzer.object;

import com.onushi.sampleapp.model.Department;
import com.onushi.sampleapp.model.Programmer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjectReadAnalyzerServiceTest {
    ObjectReadAnalyzerService objectReadAnalyzerService;
    Department department1;
    Programmer programmer1;

    @BeforeEach
    void setUp() {
        objectReadAnalyzerService = new ObjectReadAnalyzerService();
        department1 = Department.builder().id(100).name("IT").build();
        programmer1 = Programmer.builder()
                .id(1)
                .firstName("John")
                .lastName("Smith")
                .department(department1)
                .build();
    }

    @Test
    void getPublicFields() {
        List<Field> publicFields = objectReadAnalyzerService.getPublicFields(programmer1);
        assertEquals(4, publicFields.size());
        assertEquals("department", publicFields.get(0).getName());
        assertEquals("firstName", publicFields.get(1).getName());
        assertEquals("id", publicFields.get(2).getName());
        assertEquals("lastName", publicFields.get(3).getName());
    }

    @Test
    void getPublicGetters() {
        List<Method> publicGetters = objectReadAnalyzerService.getPublicGetters(programmer1);
        assertEquals(2, publicGetters.size());
        assertEquals("getSalary", publicGetters.get(0).getName());
        assertEquals("isOnline", publicGetters.get(1).getName());
    }
}
