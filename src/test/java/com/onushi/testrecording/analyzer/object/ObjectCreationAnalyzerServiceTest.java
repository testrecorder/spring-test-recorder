package com.onushi.testrecording.analyzer.object;

import com.onushi.sampleapp.*;
import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.classInfo.MatchingConstructor;
import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjectCreationAnalyzerServiceTest {

    @Test
    void canBeCreatedWithLombokBuilder() {
        ObjectCreationAnalyzerService objectCreationAnalyzerService = getObjectCreationAnalyzerService();
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
        ObjectCreationAnalyzerService objectCreationAnalyzerService = getObjectCreationAnalyzerService();
        StudentWithBuilder studentWithBuilder = StudentWithBuilder.builder()
                .firstName("John")
                .lastName(null)
                .age(30)
                .build();
        assertFalse(objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(studentWithBuilder));
    }

    @Test
    void canBeCreatedWithNoArgsConstructor() {
        ObjectCreationAnalyzerService objectCreationAnalyzerService = getObjectCreationAnalyzerService();

        Person person = new Person();
        assertTrue(objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(person));

        StudentWithDefaultInitFields studentWithDefaultInitFields = new StudentWithDefaultInitFields();
        assertTrue(objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(studentWithDefaultInitFields));
    }

    @Test
    void getMatchingConstructors() {
        StudentWithPublicFields student = new StudentWithPublicFields();
        student.firstName = "John";
        student.lastName = "Aris";
        student.age = 30;

        ObjectCreationAnalyzerService objectCreationAnalyzerService = getObjectCreationAnalyzerService();
        List<MatchingConstructor> matchingConstructors =
                objectCreationAnalyzerService.getMatchingAllArgsConstructors(student);
        assertEquals(1, matchingConstructors.size());
        assertTrue(matchingConstructors.get(0).isFieldsCouldHaveDifferentOrder());

    }

    @Test
    void getMatchingConstructors2() {
        PersonService personService = new PersonService(new PersonRepositoryImpl());
        ObjectCreationAnalyzerService objectCreationAnalyzerService = getObjectCreationAnalyzerService();
        List<MatchingConstructor> matchingConstructors =
                objectCreationAnalyzerService.getMatchingAllArgsConstructors(personService);
        assertEquals(1, matchingConstructors.size());
        assertFalse(matchingConstructors.get(0).isFieldsCouldHaveDifferentOrder());
    }

    @Test
    void canBeCreatedWithNoArgsAndFields() {
        ObjectCreationAnalyzerService objectCreationAnalyzerService = getObjectCreationAnalyzerService();

        Person person = new Person();
        assertFalse(objectCreationAnalyzerService.canBeCreatedWithNoArgsAndFields(person));

        StudentWithDefaultInitFields studentWithDefaultInitFields = new StudentWithDefaultInitFields();
        assertTrue(objectCreationAnalyzerService.canBeCreatedWithNoArgsAndFields(studentWithDefaultInitFields));
    }


    private ObjectCreationAnalyzerService getObjectCreationAnalyzerService() {
        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        ClassInfoService classInfoService = new ClassInfoService();
        ObjectStateReaderService objectStateReaderService = new ObjectStateReaderService();
        return new ObjectCreationAnalyzerService(classInfoService, objectStateReaderService);
    }
}
