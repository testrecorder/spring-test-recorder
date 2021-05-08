package com.onushi.testrecording.analyzer.object;

import com.onushi.sampleapp.*;
import com.onushi.testrecording.analyzer.classInfo.MatchingConstructor;
import com.onushi.testrecording.utils.ServiceCreatorUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectCreationAnalyzerServiceTest {

    @Test
    void canBeCreatedWithLombokBuilder() {
        ObjectCreationAnalyzerService objectCreationAnalyzerService = ServiceCreatorUtils.createObjectCreationAnalyzerService();
        StudentWithBuilder studentWithBuilder = StudentWithBuilder.builder()
                .firstName("John")
                .lastName(null)
                .age(30)
                .build();
        StudentWithPublicFields studentWithPublicFields = new StudentWithPublicFields();
        assertTrue(objectCreationAnalyzerService.canBeCreatedWithLombokBuilder(studentWithBuilder));
        assertFalse(objectCreationAnalyzerService.canBeCreatedWithLombokBuilder(studentWithPublicFields));
    }

    @Test
    void cannotBeCreatedWithNoArgsConstructor() {
        ObjectCreationAnalyzerService objectCreationAnalyzerService = ServiceCreatorUtils.createObjectCreationAnalyzerService();
        StudentWithBuilder studentWithBuilder = StudentWithBuilder.builder()
                .firstName("John")
                .lastName(null)
                .age(30)
                .build();
        Map<String, FieldValue> objectState = new ObjectStateReaderService().getObjectState(studentWithBuilder);
        assertFalse(objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(studentWithBuilder, objectState));
    }

    @Test
    void canBeCreatedWithNoArgsConstructor() {
        ObjectCreationAnalyzerService objectCreationAnalyzerService = ServiceCreatorUtils.createObjectCreationAnalyzerService();

        Person person = new Person();
        Map<String, FieldValue> objectState1 = new ObjectStateReaderService().getObjectState(person);
        assertTrue(objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(person, objectState1));

        StudentWithDefaultInitFields studentWithDefaultInitFields = new StudentWithDefaultInitFields();
        Map<String, FieldValue> objectState2 = new ObjectStateReaderService().getObjectState(person);
        assertTrue(objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(studentWithDefaultInitFields, objectState2));
    }

    @Test
    void getMatchingConstructors() {
        StudentWithPublicFields student = new StudentWithPublicFields();
        student.firstName = "John";
        student.lastName = "Aris";
        student.age = 30;

        ObjectCreationAnalyzerService objectCreationAnalyzerService = ServiceCreatorUtils.createObjectCreationAnalyzerService();
        Map<String, FieldValue> studentObjectState = new ObjectStateReaderService().getObjectState(student);
        List<MatchingConstructor> matchingConstructors =
                objectCreationAnalyzerService.getMatchingAllArgsConstructors(student, studentObjectState);
        assertEquals(1, matchingConstructors.size());
        assertTrue(matchingConstructors.get(0).isFieldsCouldHaveDifferentOrder());

    }

    @Test
    void getMatchingConstructors2() {
        PersonService personService = new PersonService(new PersonRepositoryImpl());
        Map<String, FieldValue> objectState = new ObjectStateReaderService().getObjectState(personService);
        ObjectCreationAnalyzerService objectCreationAnalyzerService = ServiceCreatorUtils.createObjectCreationAnalyzerService();
        List<MatchingConstructor> matchingConstructors =
                objectCreationAnalyzerService.getMatchingAllArgsConstructors(personService, objectState);
        assertEquals(1, matchingConstructors.size());
        assertFalse(matchingConstructors.get(0).isFieldsCouldHaveDifferentOrder());
    }

    @Test
    void canBeCreatedWithNoArgsAndFields() {
        ObjectCreationAnalyzerService objectCreationAnalyzerService = ServiceCreatorUtils.createObjectCreationAnalyzerService();

        Person person = new Person();
        Map<String, FieldValue> objectState = new ObjectStateReaderService().getObjectState(person);
        assertFalse(objectCreationAnalyzerService.canBeCreatedWithNoArgsAndFields(person, objectState));

        StudentWithDefaultInitFields studentWithDefaultInitFields = new StudentWithDefaultInitFields();
        objectState = new ObjectStateReaderService().getObjectState(studentWithDefaultInitFields);
        assertTrue(objectCreationAnalyzerService.canBeCreatedWithNoArgsAndFields(studentWithDefaultInitFields, objectState));
    }

    @Test
    void getFieldSetters() {
        // TODO IB Where I check if it's different from default? Should be some utility function for an object
        // TODO IB !!!! Where do I handle the fact that some fields could not be read? I should already return some comment
        // TODO IB !!!! Consider field default values for all the generic generators
        StudentWithSetters studentWithSetters = new StudentWithSetters();

        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(studentWithSetters);

        ObjectCreationAnalyzerService objectCreationAnalyzerService = ServiceCreatorUtils.createObjectCreationAnalyzerService();
        Map<String, SetterInfo> settersForFields = objectCreationAnalyzerService.getSettersForFields(studentWithSetters, objectState);

        assertNotNull(settersForFields.get("firstName"));
        assertEquals("setFirstName", settersForFields.get("firstName").getName());
        assertTrue(settersForFields.get("firstName").isForBuilder());

        assertNotNull(settersForFields.get("age"));
        assertEquals("setAge", settersForFields.get("age").getName());
        assertTrue(settersForFields.get("age").isForBuilder());

        assertNotNull(settersForFields.get("isolation"));
        assertEquals("setIsolation", settersForFields.get("isolation").getName());
        assertTrue(settersForFields.get("isolation").isForBuilder());

        assertNotNull(settersForFields.get("isModule"));
        assertEquals("setIsModule", settersForFields.get("isModule").getName());
        assertTrue(settersForFields.get("isModule").isForBuilder());

        assertNotNull(settersForFields.get("isOnline"));
        assertEquals("setOnline", settersForFields.get("isOnline").getName());
        assertTrue(settersForFields.get("isOnline").isForBuilder());

        assertNotNull(settersForFields.get("isOnline1"));
        assertEquals("setOnline1", settersForFields.get("isOnline1").getName());
        assertTrue(settersForFields.get("isOnline1").isForBuilder());

        assertNotNull(settersForFields.get("registered"));
        assertEquals("setRegistered", settersForFields.get("registered").getName());
        assertTrue(settersForFields.get("registered").isForBuilder());

        assertNotNull(settersForFields.get("isometric"));
        assertEquals("setIsometric", settersForFields.get("isometric").getName());
        assertTrue(settersForFields.get("isometric").isForBuilder());

        assertNotNull(settersForFields.get("is"));
        assertEquals("setIs", settersForFields.get("is").getName());
        assertTrue(settersForFields.get("is").isForBuilder());

        assertNotNull(settersForFields.get("isa"));
        assertEquals("setIsa", settersForFields.get("isa").getName());
        assertTrue(settersForFields.get("isa").isForBuilder());

        assertNotNull(settersForFields.get("isA"));
        assertEquals("setA", settersForFields.get("isA").getName());
        assertTrue(settersForFields.get("isA").isForBuilder());

        assertNotNull(settersForFields.get("otherField"));
        assertEquals("setOtherField", settersForFields.get("otherField").getName());
        assertFalse(settersForFields.get("otherField").isForBuilder());
    }
}
