package com.onushi.testrecording.analizer.object;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ObjectInfoFactoryTest {
    @Test
    void testNullObjectInfo() {
        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo(null, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "null");
    }

    @Test
    void testFloatObjectInfo() {
        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo(1f, "testFloat");
        assertEquals(objectInfo.getObjectName(), "testFloat");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "1.0f");
        assertEquals(objectInfo.getRequiredHelperObjects().size(), 0);
        assertEquals(objectInfo.getRequiredImports().size(), 0);
        assertEquals(objectInfo.getInit(), "");
    }

    @Test
    void testLongObjectInfo() {
        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo(1L, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "1L");
    }

    @Test
    void testByteObjectInfo() {
        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo((byte)11, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "(byte)11");
    }

    @Test
    void testShortObjectInfo() {
        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo((short)100, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "(short)100");
    }

    @Test
    void testCharacterObjectInfo() {
        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo('a', "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "'a'");
    }

    @Test
    void testStringObjectInfo() {
        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo("Hello World", "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "\"Hello World\"");
    }

    @Test
    void testBoolean() {
        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo(true, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "true");
    }

    @Test
    void testInt() {
        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo(2, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "2");
    }

    @Test
    void testDouble() {
        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo(2.5, "test");
        assertTrue(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "2.5");
    }

    @Test
    void testDateObjectInfo() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse("2021-01-01");

        ObjectInfoFactory objectInfoFactory = new ObjectInfoFactory();
        ObjectInfo objectInfo = objectInfoFactory.getObjectInfo(date1, "date1");
        assertFalse(objectInfo.isOnlyInline());
        assertEquals(objectInfo.getInlineCode(), "date1");
        assertEquals(objectInfo.getRequiredHelperObjects().size(), 1);
        assertEquals(objectInfo.getRequiredImports().size(), 2);
        assertNotEquals("", objectInfo.getInit());
    }
}
