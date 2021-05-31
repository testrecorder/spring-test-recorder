package com.onushi.testrecording.analyzer.object;

import com.onushi.sampleapp.model.Department;
import com.onushi.sampleapp.model.Programmer;
import com.onushi.testrecording.utils.ServiceCreatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectReadAnalyzerServiceTest {
    ObjectReadAnalyzerService objectReadAnalyzerService;

    @BeforeEach
    void setUp() {
        objectReadAnalyzerService = ServiceCreatorUtils.createObjectReadAnalyzerService();
    }

    @Test
    void getObjectReadInfo() {
        Department department1 = Department.builder().id(100).name("IT").build();
        Programmer programmer1 = Programmer.builder()
                .id(1)
                .firstName("John")
                .lastName("Smith")
                .department(department1)
                .build();

        Map<String, FieldReadInfo> objectReadInfo = objectReadAnalyzerService.getObjectReadInfo(programmer1);
        assertEquals(4, objectReadInfo.keySet().size());

        assertEquals(FieldReadType.PUBLIC_FIELD, objectReadInfo.get("id").getFieldReadType());
        assertEquals(1, objectReadInfo.get("id").getValue());
        assertNotNull(objectReadInfo.get("id").getFieldValue());
        assertNull(objectReadInfo.get("id").getGetter());

        assertEquals(FieldReadType.PUBLIC_FIELD, objectReadInfo.get("firstName").getFieldReadType());
        assertEquals("John", objectReadInfo.get("firstName").getValue());

        assertEquals(FieldReadType.PUBLIC_FIELD, objectReadInfo.get("lastName").getFieldReadType());
        assertEquals("Smith", objectReadInfo.get("lastName").getValue());

        assertEquals(FieldReadType.PUBLIC_FIELD, objectReadInfo.get("department").getFieldReadType());
    }
}
