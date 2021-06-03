package com.onushi.testrecording.codegenerator.object;

import com.onushi.sampleapp.model.*;
import com.onushi.sampleapp.services.PersonRepositoryImpl;
import com.onushi.sampleapp.services.PersonService;
import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.analyzer.object.FieldValueStatus;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import com.onushi.testrecording.utils.ServiceCreatorUtils;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;

class ObjectCodeGeneratorTest {
    TestGenerator testGenerator;
    ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;

    @BeforeEach
    void setUp() {
        testGenerator = mock(TestGenerator.class);
        objectCodeGeneratorFactoryManager = ServiceCreatorUtils.createObjectCodeGeneratorFactoryManager();
    }

    @Test
    void testNull() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator,null, "test");
        assertEquals("null", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testFloat() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator,1f, "testFloat");
        assertEquals("testFloat", objectCodeGenerator.getObjectName());
        assertEquals("1.0f", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(0, objectCodeGenerator.getRequiredImports().size());
        assertEquals("", objectCodeGenerator.getInitCode());
    }

    @Test
    void testLong() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, 1L, "test");
        assertEquals("1L", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testByte() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, (byte)11, "test");
        assertEquals("(byte)11", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testShort() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, (short)100, "test");
        assertEquals("(short)100", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testCharacter() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, 'a', "test");
        assertEquals("'a'", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testString() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, "Hello World", "test");
        assertEquals("\"Hello World\"", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testBoolean() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, true, "test");
        assertEquals("true", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testInt() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, 2, "test");
        assertEquals("2", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testDouble() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, 2.5, "test");
        assertEquals("2.5", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testDate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse("2021-01-01");

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, date1, "date1");
        assertEquals("date1", objectCodeGenerator.getInlineCode());
        assertEquals(1, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(2, objectCodeGenerator.getRequiredImports().size());
        assertNotEquals("", objectCodeGenerator.getInitCode());
    }

    @Test
    void testArray() {
        int[] array = {1, 2, 4};

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, array, "array1");
        assertEquals("array1", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(0, objectCodeGenerator.getRequiredImports().size());
        assertEquals("int[] array1 = {1, 2, 4};", objectCodeGenerator.getInitCode());
    }

    @Test
    void testList() {
        List<String> list = Arrays.asList("1", "2", "3");

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, list, "list1");
        assertEquals("list1", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(2, objectCodeGenerator.getRequiredImports().size());
        assertEquals("List<String> list1 = Arrays.asList(\"1\", \"2\", \"3\");", objectCodeGenerator.getInitCode());
    }

    @Test
    void testNotAllFieldsRead() {
        StudentWithBuilder student = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Michael")
                .age(35)
                .build();

        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        ObjectCodeGeneratorCreationContext context = new ObjectCodeGeneratorCreationContext();
        context.setTestGenerator(testGenerator);
        context.setObject(student);
        context.setObjectName("student1");
        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(student);
        objectState.values().forEach(x -> x.setFieldValueStatus(FieldValueStatus.COULD_NOT_READ));
        context.setObjectState(objectState);

        ObjectCodeGeneratorFactoryForNotRedFields objectCodeGeneratorFactoryForNotRedFields = new ObjectCodeGeneratorFactoryForNotRedFields();
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryForNotRedFields.createObjectCodeGenerator(context);
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.StudentWithBuilder", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals("// TODO Create this object\n" +
                "// StudentWithBuilder student1 = new StudentWithBuilder();", objectCodeGenerator.getInitCode());
        assertEquals("student1", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testNoArgsConstruction() {
        Person person = new Person();

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, person, "person1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.Person", objectCodeGenerator.getRequiredImports().get(0));
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

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, student, "student1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.StudentWithPublicFields", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(3, objectCodeGenerator.getDependencies().size());
        assertEquals("// TODO Check order of arguments\n" +
                "StudentWithPublicFields student1 = new StudentWithPublicFields(\"John\", \"Aris\", 30);", objectCodeGenerator.getInitCode());
        assertEquals("student1", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testAllArgsConstruction2() {
        PersonService personService = new PersonService(new PersonRepositoryImpl());

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, personService, "person1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.services.PersonService", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(1, objectCodeGenerator.getDependencies().size());
        assertEquals("PersonService person1 = new PersonService(personRepositoryImpl1);", objectCodeGenerator.getInitCode());
        assertEquals("person1", objectCodeGenerator.getInlineCode());
    }

    @Test
    void testNoArgsAndFields() {
        StudentWithPublicFields2 student = new StudentWithPublicFields2();
        student.firstName = "Fn";
        student.lastName = "Ln";
        student.age = 20;

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, student, "student1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.StudentWithPublicFields2", objectCodeGenerator.getRequiredImports().get(0));
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

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, studentWithBuilder1, "studentWithBuilder1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.StudentWithBuilder", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(3, objectCodeGenerator.getDependencies().size());
        assertEquals("studentWithBuilder1", objectCodeGenerator.getInlineCode());
        assertEquals(StringUtils.prepareForCompare(
                "StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()\n" +
                        "    .age(35)\n" +
                        "    .firstName(\"John\")\n" +
                        "    .lastName(\"Michael\")\n" +
                        "    .build();"),
                StringUtils.prepareForCompare(objectCodeGenerator.getInitCode()));
    }

    @Test
    void testCodeGeneratorWithNoArgsAndSetters() {
        StudentWithSetters studentWithSetters = new StudentWithSetters()
            .setFirstName("FN")
            .setAge(23)
            .setIsolation(2)
            .setIsModule(5)
            .setOnline(true)
            .setOnline1(false)
            .setRegistered(false)
            .setIsometric(false)
            .setIs(false)
            .setIsa(false)
            .setA(false);
        studentWithSetters.setOtherField("Other");

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, studentWithSetters, "studentWithSetters1");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.StudentWithSetters", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(12, objectCodeGenerator.getDependencies().size());
        assertEquals("studentWithSetters1", objectCodeGenerator.getInlineCode());
        assertEquals(StringUtils.prepareForCompare(
                "StudentWithSetters studentWithSetters1 = new StudentWithSetters()\n" +
                        "    .setAge(23)\n" +
                        "    .setFirstName(\"FN\")\n" +
                        "    .setIs(false)\n" +
                        "    .setA(false)\n" +
                        "    .setIsModule(5)\n" +
                        "    .setOnline(true)\n" +
                        "    .setOnline1(false)\n" +
                        "    .setIsa(false)\n" +
                        "    .setIsolation(2)\n" +
                        "    .setIsometric(false)\n" +
                        "    .setRegistered(false);\n" +
                        "studentWithSetters1.setOtherField(\"Other\");"),
                StringUtils.prepareForCompare(objectCodeGenerator.getInitCode()));
    }

    @Test
    void testCodeGeneratorWithFallback() {
        OtherStudent student = new OtherStudent();
        student.myInitSecretMethod("FN");

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, student, "student");
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.OtherStudent", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(0, objectCodeGenerator.getDependencies().size());
        assertEquals("student", objectCodeGenerator.getInlineCode());
        assertEquals(StringUtils.prepareForCompare(
                "// TODO Create this object\n" +
                     "// OtherStudent student = new OtherStudent();\n"),
                StringUtils.prepareForCompare(objectCodeGenerator.getInitCode()));
    }

    @Test
    void testNoSuchElementException() {
        ObjectCodeGenerator objectCodeGenerator =
                objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, new NoSuchElementException(), "ex");
        assertEquals("ex", objectCodeGenerator.getObjectName());
        assertEquals("ex", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("java.util.NoSuchElementException", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals("// TODO Create this object\n" +
                "// NoSuchElementException ex = new NoSuchElementException();\n", objectCodeGenerator.getInitCode());
    }

    @Test
    void testEnum() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, Color.BLUE, "test");
        assertEquals("Color.BLUE", objectCodeGenerator.getInlineCode());
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.Color", objectCodeGenerator.getRequiredImports().get(0));
    }

    @Test
    void testUUID() {
        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "uuid1");
        assertEquals("uuid1", objectCodeGenerator.getInlineCode());
        assertEquals(1, objectCodeGenerator.getRequiredImports().size());
        assertEquals("java.util.UUID", objectCodeGenerator.getRequiredImports().get(0));
        assertEquals("UUID uuid1 = UUID.fromString(\"123e4567-e89b-12d3-a456-426614174000\");", objectCodeGenerator.getInitCode());
    }

    @Test
    void testHashMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put(null, 0);
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, map, "map1");
        assertEquals("map1", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(2, objectCodeGenerator.getRequiredImports().size());
        assertEquals("Map<String, Integer> map1 = new HashMap<>();\n" +
                "map1.put(null, 0);\n" +
                "map1.put(\"1\", 1);\n" +
                "map1.put(\"2\", 2);\n" +
                "map1.put(\"3\", 3);\n", objectCodeGenerator.getInitCode());
        assertEquals("Map<String, Integer>", objectCodeGenerator.getActualClassName());
    }

    @Test
    void testHashSet() {
        Set<String> set1 = new HashSet<>();
        set1.add(null);
        set1.add("1");
        set1.add("2");
        set1.add("2");
        set1.add("3");

        ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactoryManager.createObjectCodeGenerator(testGenerator, set1, "set1");
        assertEquals("set1", objectCodeGenerator.getInlineCode());
        assertEquals(0, objectCodeGenerator.getRequiredHelperObjects().size());
        assertEquals(2, objectCodeGenerator.getRequiredImports().size());
        assertEquals("Set<String> set1 = new HashSet<>();\n" +
                "set1.add(null);\n" +
                "set1.add(\"1\");\n" +
                "set1.add(\"2\");\n" +
                "set1.add(\"3\");\n", objectCodeGenerator.getInitCode());
        assertEquals("Set<String>", objectCodeGenerator.getActualClassName());
    }
}
