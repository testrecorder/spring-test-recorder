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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ObjectInfoTest {
    TestGenerator testGenerator;
    ObjectInfoFactoryManager objectInfoFactoryManager;

    @BeforeEach
    void setUp() {
        testGenerator = mock(TestGenerator.class);
        objectInfoFactoryManager = ServiceCreatorUtils.createObjectInfoFactoryManager();
    }

    @Test
    void testNull() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator,null, "test");
        assertEquals("null", objectInfo.getInlineCode());
        // TODO IB !!!! add to all cases
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("null", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testFloat() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator,1f, "testFloat");
        assertEquals("testFloat", objectInfo.getObjectName());
        assertEquals("1.0f", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(0, objectInfo.getInitRequiredImports().size());
        assertEquals("", objectInfo.getInitCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("1.0f", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testLong() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, 1L, "test");
        assertEquals("1L", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("1L", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testByte() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, (byte)11, "test");
        assertEquals("(byte)11", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("(byte)11", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testShort() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, (short)100, "test");
        assertEquals("(short)100", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("(short)100", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testCharacter() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, 'a', "test");
        assertEquals("'a'", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("'a'", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testString() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, "Hello World", "test");
        assertEquals("\"Hello World\"", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("\"Hello World\"", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testBoolean() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, true, "test");
        assertEquals("true", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("true", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testInt() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, 2, "test");
        assertEquals("2", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("2", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testDouble() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, 2.5, "test");
        assertEquals("2.5", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("2.5", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testEnum() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, Color.BLUE, "test");
        assertEquals("Color.BLUE", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.Color", objectInfo.getInitRequiredImports().get(0));
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("Color.BLUE", objectInfo.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testUUID() {
        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "uuid1");
        assertEquals("uuid1", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("java.util.UUID", objectInfo.getInitRequiredImports().get(0));
        assertEquals("UUID uuid1 = UUID.fromString(\"123e4567-e89b-12d3-a456-426614174000\");", objectInfo.getInitCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("UUID.fromString(\"123e4567-e89b-12d3-a456-426614174000\");",
                objectInfo.visibleProperties.get("").getFinalValue().getString());
    }


    // TODO IB !!!! !!!! test all
    @Test
    void testDate() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = simpleDateFormat.parse("2021-01-01");

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, date1, "date1");
        assertEquals("date1", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(2, objectInfo.getInitRequiredImports().size());
        assertNotEquals("", objectInfo.getInitCode());
        // TODO IB !!!! add test of visible props
    }

    @Test
    void testArray() {
        int[] array = {1, 2, 4};

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, array, "array1");
        assertEquals("array1", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(0, objectInfo.getInitRequiredImports().size());
        assertEquals("int[] array1 = {1, 2, 4};", objectInfo.getInitCode());
        assertEquals(4, objectInfo.visibleProperties.size());
        assertEquals("3", objectInfo.visibleProperties.get(".length").getFinalValue().getString());
        ObjectInfo element = objectInfo.visibleProperties.get("[0]").getFinalValue().getObjectInfo();
        assertEquals("1", element.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testList() {
        List<String> list = Arrays.asList("1", "2", "3");

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, list, "list1");
        assertEquals("list1", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(2, objectInfo.getInitRequiredImports().size());
        assertEquals("List<String> list1 = Arrays.asList(\"1\", \"2\", \"3\");", objectInfo.getInitCode());
        assertEquals(4, objectInfo.visibleProperties.size());
        assertEquals("3", objectInfo.visibleProperties.get(".size()").getFinalValue().getString());
        ObjectInfo element = objectInfo.visibleProperties.get(".get(0)").getFinalValue().getObjectInfo();
        assertEquals("\"1\"", element.visibleProperties.get("").getFinalValue().getString());
    }

    @Test
    void testNotAllFieldsRead() {
        StudentWithBuilder student = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Michael")
                .age(35)
                .build();

        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        ObjectInfoCreationContext context = new ObjectInfoCreationContext();
        context.setTestGenerator(testGenerator);
        context.setObject(student);
        context.setObjectName("student1");
        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(student);
        objectState.values().forEach(x -> x.setFieldValueStatus(FieldValueStatus.COULD_NOT_READ));
        context.setObjectState(objectState);

        ObjectInfoFactoryForNotRedFields objectInfoFactoryForNotRedFields = new ObjectInfoFactoryForNotRedFields();
        ObjectInfo objectInfo = objectInfoFactoryForNotRedFields.createObjectInfo(context);
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.StudentWithBuilder", objectInfo.getInitRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals("// TODO Create this object\n" +
                "// StudentWithBuilder student1 = new StudentWithBuilder();", objectInfo.getInitCode());
        assertEquals("student1", objectInfo.getInlineCode());
    }

    @Test
    void testNoArgsConstruction() {
        Person person = new Person();

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, person, "person1");
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.Person", objectInfo.getInitRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals("Person person1 = new Person();", objectInfo.getInitCode());
        assertEquals("person1", objectInfo.getInlineCode());

        assertEquals(3, objectInfo.visibleProperties.size());
        ObjectInfo element = objectInfo.visibleProperties.get(".getFirstName()").getFinalValue().getObjectInfo();
        assertEquals("null", element.visibleProperties.get("").getFinalValue().getString());
        Method method = objectInfo.visibleProperties.get(".getFirstName()").getPropertySource().getGetter();
        assertNotNull(method);
        assertEquals("getFirstName", method.getName());
    }

    @Test
    void testAllArgsConstruction() {
        StudentWithPublicFields student = new StudentWithPublicFields();
        student.firstName = "John";
        student.lastName = "Aris";
        student.age = 30;

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, student, "student1");
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.StudentWithPublicFields", objectInfo.getInitRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(3, objectInfo.getInitDependencies().size());
        assertEquals("// TODO Check order of arguments\n" +
                "StudentWithPublicFields student1 = new StudentWithPublicFields(\"John\", \"Aris\", 30);", objectInfo.getInitCode());
        assertEquals("student1", objectInfo.getInlineCode());
    }

    @Test
    void testAllArgsConstruction2() {
        PersonService personService = new PersonService(new PersonRepositoryImpl());

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, personService, "person1");
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("com.onushi.sampleapp.services.PersonService", objectInfo.getInitRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(1, objectInfo.getInitDependencies().size());
        assertEquals("PersonService person1 = new PersonService(personRepositoryImpl1);", objectInfo.getInitCode());
        assertEquals("person1", objectInfo.getInlineCode());
    }

    @Test
    void testNoArgsAndFields() {
        StudentWithPublicFields2 student = new StudentWithPublicFields2();
        student.firstName = "Fn";
        student.lastName = "Ln";
        student.age = 20;

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, student, "student1");
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.StudentWithPublicFields2", objectInfo.getInitRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(3, objectInfo.getInitDependencies().size());
        assertEquals("StudentWithPublicFields2 student1 = new StudentWithPublicFields2();\n" +
                "student1.firstName = \"Fn\";\n" +
                "student1.lastName = \"Ln\";\n" +
                "student1.age = 20;\n", objectInfo.getInitCode());
        assertEquals("student1", objectInfo.getInlineCode());

        assertEquals(3, objectInfo.visibleProperties.size());
        ObjectInfo element = objectInfo.visibleProperties.get(".firstName").getFinalValue().getObjectInfo();
        assertEquals("\"Fn\"", element.visibleProperties.get("").getFinalValue().getString());
        Field field = objectInfo.visibleProperties.get(".firstName").getPropertySource().getField();
        assertNotNull(field);
        assertEquals("firstName", field.getName());
    }

    @Test
    void testObjectInfoWithBuilder() {
        StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Michael")
                .age(35)
                .build();

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, studentWithBuilder1, "studentWithBuilder1");
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.StudentWithBuilder", objectInfo.getInitRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(3, objectInfo.getInitDependencies().size());
        assertEquals("studentWithBuilder1", objectInfo.getInlineCode());
        assertEquals(StringUtils.prepareForCompare(
                "StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()\n" +
                        "    .age(35)\n" +
                        "    .firstName(\"John\")\n" +
                        "    .lastName(\"Michael\")\n" +
                        "    .build();"),
                StringUtils.prepareForCompare(objectInfo.getInitCode()));
    }

    @Test
    void testObjectInfoWithNoArgsAndSetters() {
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

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, studentWithSetters, "studentWithSetters1");
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.StudentWithSetters", objectInfo.getInitRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(12, objectInfo.getInitDependencies().size());
        assertEquals("studentWithSetters1", objectInfo.getInlineCode());
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
                StringUtils.prepareForCompare(objectInfo.getInitCode()));
    }

    @Test
    void testObjectInfoWithFallback() {
        OtherStudent student = new OtherStudent();
        student.myInitSecretMethod("FN");

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, student, "student");
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("com.onushi.sampleapp.model.OtherStudent", objectInfo.getInitRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(0, objectInfo.getInitDependencies().size());
        assertEquals("student", objectInfo.getInlineCode());
        assertEquals(StringUtils.prepareForCompare(
                "// TODO Create this object\n" +
                     "// OtherStudent student = new OtherStudent();\n"),
                StringUtils.prepareForCompare(objectInfo.getInitCode()));
    }

    @Test
    void testNoSuchElementException() {
        ObjectInfo objectInfo =
                objectInfoFactoryManager.createObjectInfo(testGenerator, new NoSuchElementException(), "ex");
        assertEquals("ex", objectInfo.getObjectName());
        assertEquals("ex", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("java.util.NoSuchElementException", objectInfo.getInitRequiredImports().get(0));
        assertEquals("// TODO Create this object\n" +
                "// NoSuchElementException ex = new NoSuchElementException();\n", objectInfo.getInitCode());
    }

    @Test
    void testHashMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put(null, 0);
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, map, "map1");
        assertEquals("map1", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(2, objectInfo.getInitRequiredImports().size());
        assertEquals("Map<String, Integer> map1 = new HashMap<>();\n" +
                "map1.put(null, 0);\n" +
                "map1.put(\"1\", 1);\n" +
                "map1.put(\"2\", 2);\n" +
                "map1.put(\"3\", 3);\n", objectInfo.getInitCode());
        assertEquals("Map<String, Integer>", objectInfo.getActualClassName());
    }

    @Test
    void testHashSet() {
        Set<String> set1 = new HashSet<>();
        set1.add(null);
        set1.add("1");
        set1.add("2");
        set1.add("2");
        set1.add("3");

        ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, set1, "set1");
        assertEquals("set1", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(2, objectInfo.getInitRequiredImports().size());
        assertEquals("Set<String> set1 = new HashSet<>();\n" +
                "set1.add(null);\n" +
                "set1.add(\"1\");\n" +
                "set1.add(\"2\");\n" +
                "set1.add(\"3\");\n", objectInfo.getInitCode());
        assertEquals("Set<String>", objectInfo.getActualClassName());
    }
}
