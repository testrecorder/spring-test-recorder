package com.onushi.testrecording.analizer.object;

import com.onushi.testrecording.analizer.utils.ClassService;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ObjectInfoServiceTest {
    @Test
    void testNullObjectInfo() {
        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(null, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "null");
    }

    @Test
    void testFloatObjectInfo() {
        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(1f, "testFloat");
        assertEquals(objectInfo.getObjectName(), "testFloat");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "1.0f");
        assertEquals(objectInfo.getRequiredHelperObjects().size(), 0);
        assertEquals(objectInfo.getRequiredImports().size(), 0);
        assertEquals(objectInfo.getInit(), "");
    }

    @Test
    void testLongObjectInfo() {
        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(1L, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "1L");
    }

    @Test
    void testByteObjectInfo() {
        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo((byte)11, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "(byte)11");
    }

    @Test
    void testShortObjectInfo() {
        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo((short)100, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "(short)100");
    }

    @Test
    void testCharacterObjectInfo() {
        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo('a', "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "'a'");
    }

    @Test
    void testStringObjectInfo() {
        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo("Hello World", "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "\"Hello World\"");
    }

    @Test
    void testBoolean() {
        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(true, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "true");
    }

    @Test
    void testInt() {
        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(2, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "2");
    }

    @Test
    void testDouble() {
        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(2.5, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "2.5");
    }

    @Test
    void testDateObjectInfo() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse("2021-01-01");

        ObjectInfoService objectInfoService = getObjectInfoService();
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(date1, "date1");
        assertFalse(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "date1");
        assertEquals(objectInfo.getRequiredHelperObjects().size(), 1);
        assertEquals(objectInfo.getRequiredImports().size(), 2);
        assertNotEquals("", objectInfo.getInit());
    }

    private ObjectInfoService getObjectInfoService() {
        return new ObjectInfoService(new ClassService());
    }
}
