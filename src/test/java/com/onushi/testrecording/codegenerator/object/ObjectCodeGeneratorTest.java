package com.onushi.testrecording.codegenerator.object;

import com.onushi.sampleapp.*;
import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.test.*;
import com.onushi.testrecording.utils.StringUtils;
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
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator,null, "test");
        assertEquals("null", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testFloat() {
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator,1f, "testFloat");
        assertEquals("testFloat", objectCodeGenerator.getObjectName());
        assertEquals("1.0f", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(0, objectCodeGenerator.getRequiredImports().size());
        assertEquals("", objectCodeGenerator.getInitCode());
    }

    @Test
    void testLong() {
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, 1L, "test");
        assertEquals("1L", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testByte() {
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, (byte)11, "test");
        assertEquals("(byte)11", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testShort() {
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, (short)100, "test");
        assertEquals("(short)100", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testCharacter() {
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, 'a', "test");
        assertEquals("'a'", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testString() {
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, "Hello World", "test");
        assertEquals("\"Hello World\"", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testBoolean() {
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, true, "test");
        assertEquals("true", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testInt() {
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, 2, "test");
        assertEquals("2", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testDouble() {
        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, 2.5, "test");
        assertEquals("2.5", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testDate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse("2021-01-01");

        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, date1, "date1");
        assertEquals("date1", objectCodeGenerator.getInlineCode());
        assertEquals(1, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(2, objectCodeGenerator.getRequiredImports().size());
        assertNotEquals("", objectCodeGenerator.getInitCode());
    }

    @Test
    void testArray() {
        int[] array = {1, 2, 4};

        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, array, "array1");
        assertEquals("array1", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(0, objectCodeGenerator.getRequiredImports().size());
        assertEquals("int[] array1 = {1, 2, 4};", objectCodeGenerator.getInitCode());
    }

    @Test
    void testList() {
        List<String> list = Arrays.asList("1", "2", "3");

        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, list, "list1");
        assertEquals("list1", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(2, objectCodeGenerator.getRequiredImports().size());
        assertEquals("List<String> list1 = Arrays.asList(\"1\", \"2\", \"3\");", objectCodeGenerator.getInitCode());
    }

    @Test
    void testNoArgsConstruction() {
        Person person = new Person();

        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, person, "person1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.Person", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals("Person person1 = new Person();", objectCodeGenerator.getInitCode());
        assertEquals("person1", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testAllArgsConstruction() {
        StudentWithPublicFields student = new StudentWithPublicFields();
        student.firstName = "John";
        student.lastName = "Aris";
        student.age = 30;

        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, student, "student1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.StudentWithPublicFields", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(3, objectCodeGenerator.getDependencies().size());
        assertEquals("// TODO Check order of arguments\n" +
                "StudentWithPublicFields student1 = new StudentWithPublicFields(\"John\", \"Aris\", 30);", objectCodeGenerator.getInitCode());
        assertEquals("student1", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testAllArgsConstruction2() {
        PersonService personService = new PersonService(new PersonRepositoryImpl());

        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, personService, "person1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.PersonService", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(1, objectCodeGenerator.getDependencies().size());
        assertEquals("PersonService person1 = new PersonService(personRepositoryImpl1);", objectCodeGenerator.getInitCode());
        assertEquals("person1", objectCodeGenerator.getInlineCode());
    }

    // TODO IB !!!! test construction with no args + setters

    @Test
    void testNoArgsAndFields() {
        StudentWithPublicFields2 student = new StudentWithPublicFields2();
        student.firstName = "Fn";
        student.lastName = "Ln";
        student.age = 20;

        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, student, "student1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.StudentWithPublicFields2", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(3, objectCodeGenerator.getDependencies().size());
        assertEquals("StudentWithPublicFields2 student1 = new StudentWithPublicFields2();\n" +
                "student1.firstName = \"Fn\";\n" +
                "student1.lastName = \"Ln\";\n" +
                "student1.age = 20;\n", objectCodeGenerator.getInitCode());
        assertEquals("student1", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testCodeGeneratorWithBuilder() {
        StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Michael")
                .age(35)
                .build();

        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, studentWithBuilder1, "studentWithBuilder1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.StudentWithBuilder", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(3, objectCodeGenerator.getDependencies().size());
        assertEquals("studentWithBuilder1", objectCodeGenerator.getInlineCode());
        assertEquals(StringUtils.trimAndIgnoreCRDiffs(
                "StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()\n" +
                        "    .age(35)\n" +
                        "    .firstName(\"John\")\n" +
                        "    .lastName(\"Michael\")\n" +
                        "    .build();"),
                StringUtils.trimAndIgnoreCRDiffs(objectCodeGenerator.getInitCode()));
    }

    @Test
    void testCodeGeneratorWithFallback() {
        OtherStudent student = new OtherStudent();
        student.myInitSecretMethod("FN");

        ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager = getObjectCodeGeneratorFactoryManager();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, student, "student");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.OtherStudent", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(1, objectCodeGenerator.getDependencies().size());
        assertEquals("student", objectCodeGenerator.getInlineCode());
        assertEquals(StringUtils.trimAndIgnoreCRDiffs(
                "// TODO Create this object\n" +
                     "// OtherStudent student = new OtherStudent();\n" +
                     "// student.firstName = \"FN\";\n"),
                StringUtils.trimAndIgnoreCRDiffs(objectCodeGenerator.getInitCode()));
    }

    private ObjectCodeGeneratorFactoryManager getObjectCodeGeneratorFactoryManager() {
        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        ClassInfoService classInfoService = new ClassInfoService();
        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        return new ObjectCodeGeneratorFactoryManager(classInfoService,
                objectStateReaderService,
                objectNameGenerator,
                new ObjectCreationAnalyzerService(classInfoService, objectStateReaderService));
    }
}
