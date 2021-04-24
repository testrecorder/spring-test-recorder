package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ObjectCodeGeneratorTest {
    @Test
    void testNull() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(null, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "null");
    }

    @Test
    void testFloat() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(1f, "testFloat");
        assertEquals(objectCodeGenerator.getObjectName(), "testFloat");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "1.0f");
        assertEquals(objectCodeGenerator.getRequiredHelperObjects().size(), 0);
        assertEquals(objectCodeGenerator.getRequiredImports().size(), 0);
        assertEquals(objectCodeGenerator.getInitCode(), "");
    }

    @Test
    void testLong() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(1L, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "1L");
    }

    @Test
    void testByte() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator((byte)11, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "(byte)11");
    }

    @Test
    void testShort() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator((short)100, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "(short)100");
    }

    @Test
    void testCharacter() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator('a', "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "'a'");
    }

    @Test
    void testString() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator("Hello World", "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "\"Hello World\"");
    }

    @Test
    void testBoolean() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(true, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "true");
    }

    @Test
    void testInt() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(2, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "2");
    }

    @Test
    void testDouble() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(2.5, "test");
        assertTrue(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "2.5");
    }

    @Test
    void testDate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse("2021-01-01");

        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = new ObjectCodeGeneratorFactory(new ClassInfoService());
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(date1, "date1");
        assertFalse(objectCodeGenerator.isOnlyInline());
        assertEquals(objectCodeGenerator.getInlineCode(), "date1");
        assertEquals(objectCodeGenerator.getRequiredHelperObjects().size(), 1);
        assertEquals(objectCodeGenerator.getRequiredImports().size(), 2);
        assertNotEquals("", objectCodeGenerator.getInitCode());
    }
}
