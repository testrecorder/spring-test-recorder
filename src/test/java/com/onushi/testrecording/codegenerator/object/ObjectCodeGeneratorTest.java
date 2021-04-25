package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ObjectCodeGeneratorTest {
    @Test
    void testNull() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(null, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals("null", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testFloat() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(1f, "testFloat");
        assertEquals("testFloat", objectCodeGenerator.getObjectName());
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals("1.0f", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(0, objectCodeGenerator.getRequiredImports().size());
        assertEquals("", objectCodeGenerator.getInitCode());
    }

    @Test
    void testLong() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(1L, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals("1L", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testByte() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator((byte)11, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals("(byte)11", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testShort() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator((short)100, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals("(short)100", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testCharacter() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator('a', "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals("'a'", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testString() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator("Hello World", "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals("\"Hello World\"", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testBoolean() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(true, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals("true", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testInt() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(2, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals("2", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testDouble() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(2.5, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals("2.5", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testDate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse("2021-01-01");

        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(date1, "date1");
        assertFalse(objectCodeGenerator.isOnlyInline());
        assertEquals("date1", objectCodeGenerator.getInlineCode());
        assertEquals(1, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(2, objectCodeGenerator.getRequiredImports().size());
        // TODO IB !!!! check also assertNotEquals
        assertNotEquals("", objectCodeGenerator.getInitCode());
    }

    private ObjectCodeGeneratorFactory getObjectCodeGeneratorFactory() {
        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        return new ObjectCodeGeneratorFactory(new ClassInfoService(), new ObjectStateReaderService(objectNameGenerator));
    }
}
