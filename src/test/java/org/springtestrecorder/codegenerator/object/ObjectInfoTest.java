/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sample.model.Color;
import org.sample.model.OtherStudent;
import org.sample.model.Person;
import org.sample.model.PersonWithProtectedNoArgsConstructor;
import org.sample.model.StudentWithBuilder;
import org.sample.model.StudentWithPackageAndProtectedSetters;
import org.sample.model.StudentWithProtectedAllArgsConstructor;
import org.sample.model.StudentWithPublicFields;
import org.sample.model.StudentWithPublicFields2;
import org.sample.model.StudentWithSetters;
import org.sample.services.PersonRepositoryImpl;
import org.sample.services.PersonService;
import org.springtestrecorder.analyzer.object.FieldValue;
import org.springtestrecorder.analyzer.object.FieldValueStatus;
import org.springtestrecorder.analyzer.object.ObjectStateReaderService;
import org.springtestrecorder.codegenerator.test.TestGenerator;
import org.springtestrecorder.codegenerator.test.TestRecordingPhase;
import org.springtestrecorder.utils.ServiceCreatorUtils;
import org.springtestrecorder.utils.StringUtils;

class ObjectInfoTest {
    TestGenerator testGenerator;
    ObjectInfoFactoryManager objectInfoFactoryManager;

    @BeforeEach
    void setUp() {
        testGenerator = mock(TestGenerator.class);
        when(testGenerator.getPackageName()).thenReturn("org.sample.model");
        when(testGenerator.getCurrentTestRecordingPhase()).thenReturn(TestRecordingPhase.BEFORE_METHOD_RUN);
        objectInfoFactoryManager = ServiceCreatorUtils.createObjectInfoFactoryManager();
    }

    @Test
    void testNull() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, null, "test");
        assertEquals("null", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("null", getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testFloat() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, 1f, "testFloat");
        assertEquals("testFloat", objectInfo.getObjectName());
        assertEquals("1.0f", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(0, objectInfo.getDeclareRequiredImports().size());
        assertEquals("", objectInfo.getInitCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("1.0f", getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testLong() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, 1L, "test");
        assertEquals("1L", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("1L", getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testByte() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, (byte) 11, "test");
        assertEquals("(byte)11", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("(byte)11", getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testShort() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, (short) 100, "test");
        assertEquals("(short)100", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("(short)100", getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testCharacter() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, 'a', "test");
        assertEquals("'a'", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("'a'", getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testString() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, "Hello World", "test");
        assertEquals("\"Hello World\"", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("\"Hello World\"", getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testBoolean() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, true, "test");
        assertEquals("true", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("true", getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testInt() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, 2, "test");
        assertEquals("2", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("2", getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testDouble() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, 2.5, "test");
        assertEquals("2.5", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("2.5", getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testEnum() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, Color.BLUE, "test");
        assertEquals("Color.BLUE", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("org.sample.model.Color", objectInfo.getDeclareRequiredImports().get(0));
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("Color.BLUE", getKeySnapshot(objectInfo, "").getValue().getString());
        assertEquals(1, getKeySnapshot(objectInfo, "").getRequiredImports().size());
        assertEquals("org.sample.model.Color", getKeySnapshot(objectInfo, "").getRequiredImports().get(0));
    }

    @Test
    void testUUID() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "uuid1");
        assertEquals("uuid1", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("java.util.UUID", objectInfo.getDeclareRequiredImports().get(0));
        assertEquals("UUID uuid1 = UUID.fromString(\"123e4567-e89b-12d3-a456-426614174000\");", objectInfo.getInitCode());
        assertEquals(1, objectInfo.visibleProperties.size());
        assertEquals("UUID.fromString(\"123e4567-e89b-12d3-a456-426614174000\")",
                getKeySnapshot(objectInfo, "").getValue().getString());
    }

    @Test
    void testDate() throws Exception {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date date1 = simpleDateFormat.parse("2021-01-01");

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, date1, "date1");
        assertEquals("date1", objectInfo.getInlineCode());
        assertEquals(1, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertNotEquals("", objectInfo.getInitCode());

        assertEquals(1, objectInfo.visibleProperties.size());
        final VisibleProperty visibleProperty = objectInfo.visibleProperties.get("");
        assertNotNull(visibleProperty);
        assertEquals(1, visibleProperty.snapshots.size());
        assertNotNull(visibleProperty.snapshots.get(TestRecordingPhase.BEFORE_METHOD_RUN));

        final VisiblePropertySnapshot snapshot = visibleProperty.snapshots.get(TestRecordingPhase.BEFORE_METHOD_RUN);
        assertEquals(1, snapshot.getRequiredImports().size());
        assertEquals("java.text.SimpleDateFormat", snapshot.getRequiredImports().get(0));
        assertEquals(1, snapshot.getRequiredHelperObjects().size());
        Assertions.assertEquals(ObjectInfoFactoryForDateImpl.SIMPLE_DATE_FORMAT_HELPER, snapshot.getRequiredHelperObjects().get(0));
        assertEquals("simpleDateFormat.parse(\"2021-01-01 00:00:00.000\")", snapshot.getValue().getString());
    }

    @Test
    void testArray() {
        final int[] array = { 1, 2, 4 };

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, array, "array1");
        assertEquals("array1", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(0, objectInfo.getDeclareRequiredImports().size());
        assertEquals("int[] array1 = {1, 2, 4};", objectInfo.getInitCode());
        assertEquals(4, objectInfo.visibleProperties.size());
        assertEquals("3", getKeySnapshot(objectInfo, ".length").getValue().getString());
        final ObjectInfo element = getKeySnapshot(objectInfo, "[0]").getValue().getObjectInfo();
        assertEquals("1", getKeySnapshot(element, "").getValue().getString());
    }

    @Test
    void testList() {
        final List<String> list = Arrays.asList("1", "2", "3");

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, list, "list1");
        assertEquals("list1", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(2, objectInfo.getDeclareRequiredImports().size());
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("List<String> list1 = new ArrayList<>(Arrays.asList(\"1\", \"2\", \"3\"));", objectInfo.getInitCode());
        assertEquals(4, objectInfo.visibleProperties.size());
        assertEquals("3", getKeySnapshot(objectInfo, ".size()").getValue().getString());
        final ObjectInfo element = getKeySnapshot(objectInfo, ".get(0)").getValue().getObjectInfo();
        assertEquals("\"1\"", getKeySnapshot(element, "").getValue().getString());
    }

    @Test
    void testNotAllFieldsRead() {
        final StudentWithBuilder student = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Michael")
                .age(35)
                .build();

        final ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        final ObjectInfoCreationContext context = new ObjectInfoCreationContext();
        context.setTestGenerator(testGenerator);
        context.setObject(student);
        context.setObjectName("student1");
        final Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(student);
        objectState.values().forEach(x -> x.setFieldValueStatus(FieldValueStatus.COULD_NOT_READ));
        context.setObjectState(objectState);

        final ObjectInfoFactoryForNotRedFields objectInfoFactoryForNotRedFields = new ObjectInfoFactoryForNotRedFields();
        final ObjectInfo objectInfo = objectInfoFactoryForNotRedFields.createObjectInfo(context);
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("org.sample.model.StudentWithBuilder", objectInfo.getDeclareRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals("// TODO Create this object (Could not read object fields)\n" +
                "// StudentWithBuilder student1 = new StudentWithBuilder();", objectInfo.getInitCode());
        assertEquals("student1", objectInfo.getInlineCode());
    }

    @Test
    void testNoArgsConstruction() {
        final Person person = new Person();

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, person, "person1");
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("org.sample.model.Person", objectInfo.getDeclareRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals("Person person1 = new Person();\n", objectInfo.getInitCode());
        assertEquals("person1", objectInfo.getInlineCode());

        assertEquals(3, objectInfo.visibleProperties.size());
        final ObjectInfo element = getKeySnapshot(objectInfo, ".getFirstName()").getValue().getObjectInfo();
        assertEquals("null", getKeySnapshot(element, "").getValue().getString());
    }

    @Test
    void testProtectedNoArgsConstruction() {
        final PersonWithProtectedNoArgsConstructor person = PersonWithProtectedNoArgsConstructor.createService();
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, person, "person1");
        assertEquals("PersonWithProtectedNoArgsConstructor person1 = new PersonWithProtectedNoArgsConstructor();\n", objectInfo.getInitCode());
    }

    @Test
    void testProtectedNoArgsAndSetters() {
        final StudentWithPackageAndProtectedSetters student = StudentWithPackageAndProtectedSetters.createStudent();
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, student, "student1");
        assertEquals("StudentWithPackageAndProtectedSetters student1 = new StudentWithPackageAndProtectedSetters()\n" +
                "    .setFirstName(\"fn\")\n" +
                "    .setLastName(\"ln\");\n\n", objectInfo.getInitCode());
    }

    @Test
    void testProtectedAllArgsConstructor() {
        final StudentWithProtectedAllArgsConstructor student = StudentWithProtectedAllArgsConstructor.createStudent("John", "Snow");
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, student, "student1");
        assertEquals("// TODO Check order of arguments\n" +
                "StudentWithProtectedAllArgsConstructor student1 = new StudentWithProtectedAllArgsConstructor(\"John\", \"Snow\");\n",
                objectInfo.getInitCode());
    }

    @Test
    void testAllArgsConstruction() {
        final StudentWithPublicFields student = new StudentWithPublicFields();
        student.firstName = "John";
        student.lastName = "Aris";
        student.age = 30;

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, student, "student1");
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("org.sample.model.StudentWithPublicFields", objectInfo.getDeclareRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(3, objectInfo.getInitDependencies().size());
        assertEquals("// TODO Check order of arguments\n" +
                "StudentWithPublicFields student1 = new StudentWithPublicFields(\"John\", \"Aris\", 30);\n", objectInfo.getInitCode());
        assertEquals("student1", objectInfo.getInlineCode());
    }

    @Test
    void testAllArgsConstruction2() {
        final PersonService personService = new PersonService(new PersonRepositoryImpl());

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, personService, "person1");
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("org.sample.services.PersonService", objectInfo.getDeclareRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(1, objectInfo.getInitDependencies().size());
        assertEquals("PersonService person1 = new PersonService(personRepositoryImpl1);\n", objectInfo.getInitCode());
        assertEquals("person1", objectInfo.getInlineCode());
    }

    @Test
    void testNoArgsAndFields() {
        final StudentWithPublicFields2 student = new StudentWithPublicFields2();
        student.firstName = "Fn";
        student.lastName = "Ln";
        student.age = 20;

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, student, "student1");
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("org.sample.model.StudentWithPublicFields2", objectInfo.getDeclareRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(3, objectInfo.getInitDependencies().size());
        assertEquals("StudentWithPublicFields2 student1 = new StudentWithPublicFields2();\n" +
                "student1.firstName = \"Fn\";\n" +
                "student1.lastName = \"Ln\";\n" +
                "student1.age = 20;\n", objectInfo.getInitCode());
        assertEquals("student1", objectInfo.getInlineCode());

        assertEquals(3, objectInfo.visibleProperties.size());
        final ObjectInfo element = getKeySnapshot(objectInfo, ".firstName").getValue().getObjectInfo();
        assertEquals("\"Fn\"", getKeySnapshot(element, "").getValue().getString());
    }

    @Test
    void testObjectInfoWithBuilder() {
        final StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()
                .firstName("John")
                .lastName("Michael")
                .age(35)
                .build();

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, studentWithBuilder1, "studentWithBuilder1");
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("org.sample.model.StudentWithBuilder", objectInfo.getDeclareRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(3, objectInfo.getInitDependencies().size());
        assertEquals("studentWithBuilder1", objectInfo.getInlineCode());
        Assertions.assertEquals(StringUtils.prepareForCompare(
                "StudentWithBuilder studentWithBuilder1 = StudentWithBuilder.builder()\n" +
                        "    .age(35)\n" +
                        "    .firstName(\"John\")\n" +
                        "    .lastName(\"Michael\")\n" +
                        "    .build();"),
                StringUtils.prepareForCompare(objectInfo.getInitCode()));
    }

    @Test
    void testObjectInfoWithNoArgsAndSetters() {
        final StudentWithSetters studentWithSetters = new StudentWithSetters()
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

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, studentWithSetters, "studentWithSetters1");
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("org.sample.model.StudentWithSetters", objectInfo.getDeclareRequiredImports().get(0));
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
        final OtherStudent student = new OtherStudent();
        student.myInitSecretMethod("FN");

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, student, "student");
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("org.sample.model.OtherStudent", objectInfo.getDeclareRequiredImports().get(0));
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(0, objectInfo.getInitDependencies().size());
        assertEquals("student", objectInfo.getInlineCode());
        assertEquals(StringUtils.prepareForCompare(
                "// TODO Create this object (Could not create generic object)\n" +
                        "// OtherStudent student = new OtherStudent();\n"),
                StringUtils.prepareForCompare(objectInfo.getInitCode()));
    }

    @Test
    void testNoSuchElementException() {
        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, new NoSuchElementException(), "ex");
        assertEquals("ex", objectInfo.getObjectName());
        assertEquals("ex", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals("java.util.NoSuchElementException", objectInfo.getDeclareRequiredImports().get(0));
        assertEquals("// TODO Create this object (Could not create generic object)\n" +
                "// NoSuchElementException ex = new NoSuchElementException();\n", objectInfo.getInitCode());
    }

    @Test
    void testHashMap() {
        final Map<String, Integer> map = new HashMap<>();
        map.put(null, 0);
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, map, "map1");
        assertEquals("map1", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("Map<String, Integer> map1 = new HashMap<>();\n" +
                "map1.put(null, 0);\n" +
                "map1.put(\"1\", 1);\n" +
                "map1.put(\"2\", 2);\n" +
                "map1.put(\"3\", 3);\n", objectInfo.getInitCode());
        assertEquals("Map<String, Integer>", objectInfo.getComposedClassNameForDeclare());
        assertEquals(5, objectInfo.visibleProperties.size());
        assertEquals("4", getKeySnapshot(objectInfo, ".size()").getValue().getString());
        assertEquals("0", getKeySnapshot(getKeySnapshot(objectInfo, ".get(null)").getValue().getObjectInfo(), "").getValue().getString());
        assertEquals("1", getKeySnapshot(getKeySnapshot(objectInfo, ".get(\"1\")").getValue().getObjectInfo(), "").getValue().getString());
        assertEquals("2", getKeySnapshot(getKeySnapshot(objectInfo, ".get(\"2\")").getValue().getObjectInfo(), "").getValue().getString());
        assertEquals("3", getKeySnapshot(getKeySnapshot(objectInfo, ".get(\"3\")").getValue().getObjectInfo(), "").getValue().getString());
    }

    @Test
    void testHashSet() {
        final Set<String> set1 = new HashSet<>();
        set1.add(null);
        set1.add("1");
        set1.add("2");
        set1.add("3");

        final ObjectInfo objectInfo = objectInfoFactoryManager.createObjectInfo(testGenerator, set1, "set1");
        assertEquals("set1", objectInfo.getInlineCode());
        assertEquals(0, objectInfo.getInitRequiredHelperObjects().size());
        assertEquals(1, objectInfo.getDeclareRequiredImports().size());
        assertEquals(1, objectInfo.getInitRequiredImports().size());
        assertEquals("Set<String> set1 = new HashSet<>();\n" +
                "set1.add(null);\n" +
                "set1.add(\"1\");\n" +
                "set1.add(\"2\");\n" +
                "set1.add(\"3\");\n", objectInfo.getInitCode());
        assertEquals("Set<String>", objectInfo.getComposedClassNameForDeclare());
        assertEquals(5, objectInfo.visibleProperties.size());
        assertEquals("4", getKeySnapshot(objectInfo, ".size()").getValue().getString());
        assertEquals("true", getKeySnapshot(objectInfo, ".contains(null)").getValue().getString());
        assertEquals("true", getKeySnapshot(objectInfo, ".contains(\"1\")").getValue().getString());
        assertEquals("true", getKeySnapshot(objectInfo, ".contains(\"2\")").getValue().getString());
        assertEquals("true", getKeySnapshot(objectInfo, ".contains(\"3\")").getValue().getString());
    }

    private VisiblePropertySnapshot getKeySnapshot(final ObjectInfo objectInfo, final String key) {
        return objectInfo.visibleProperties.get(key).getSnapshots().get(TestRecordingPhase.BEFORE_METHOD_RUN);
    }
}
