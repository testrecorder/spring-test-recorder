package com.onushi.testrecording.codegenerator.object;

import com.onushi.sampleapp.Person;
import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.test.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ObjectCodeGeneratorTest {
    TestGenerator testGenerator;

    @BeforeEach
    void setUp() {
        testGenerator = mock(TestGenerator.class);
    }

    @Test
    void testNull() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator,null, "test");
        assertEquals("null", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testFloat() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator,1f, "testFloat");
        assertEquals("testFloat", objectCodeGenerator.getObjectName());
        assertEquals("1.0f", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(0, objectCodeGenerator.getRequiredImports().size());
        assertEquals("", objectCodeGenerator.getInitCode());
    }

    @Test
    void testLong() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, 1L, "test");
        assertEquals("1L", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testByte() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, (byte)11, "test");
        assertEquals("(byte)11", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testShort() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, (short)100, "test");
        assertEquals("(short)100", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testCharacter() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, 'a', "test");
        assertEquals("'a'", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testString() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, "Hello World", "test");
        assertEquals("\"Hello World\"", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testBoolean() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, true, "test");
        assertEquals("true", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testInt() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, 2, "test");
        assertEquals("2", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testDouble() {
        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, 2.5, "test");
        assertEquals("2.5", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testDate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse("2021-01-01");

        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, date1, "date1");
        assertEquals("date1", objectCodeGenerator.getInlineCode());
        assertEquals(1, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(2, objectCodeGenerator.getRequiredImports().size());
        assertNotEquals("", objectCodeGenerator.getInitCode());
    }

    @Test
    void testArray() {
        int[] array = {1, 2, 4};

        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, array, "array1");
        assertEquals("array1", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(0, objectCodeGenerator.getRequiredImports().size());
        assertEquals("int[] array1 = {1, 2, 4};", objectCodeGenerator.getInitCode());
    }

    @Test
    void testList() {
        List<String> list = Arrays.asList("1", "2", "3");

        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, list, "list1");
        assertEquals("list1", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(2, objectCodeGenerator.getRequiredImports().size());
        assertEquals("List<String> list1 = Arrays.asList(\"1\", \"2\", \"3\");", objectCodeGenerator.getInitCode());
    }

    @Test
    void testNoArgsConstruction() {
        Person person = new Person();

        ObjectCodeGeneratorFactory objectCodeGeneratorFactory = getObjectCodeGeneratorFactory();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, person, "person1");
        assertEquals("new Person()", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.Person", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals("", objectCodeGenerator.getInitCode());
    }

    // TODO IB !!!! test construction if there is a identifiable constructor covering all the fields

    // TODO IB !!!! test construction with no args + setters

    // TODO IB !!!! test construction with no args + public fields

    private ObjectCodeGeneratorFactory getObjectCodeGeneratorFactory() {
        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        ClassInfoService classInfoService = new ClassInfoService();
        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        return new ObjectCodeGeneratorFactory(classInfoService,
                objectStateReaderService,
                objectNameGenerator,
                new ObjectCreationAnalyzerService(classInfoService, objectStateReaderService));
    }
}
