package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.sample.model.Department;
import com.onushi.sample.model.Employee;
import com.onushi.sample.model.Person;
import com.onushi.sample.model.SimplePerson;
import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.codegenerator.test.TestGenerator;
import com.onushi.springtestrecorder.codegenerator.test.TestGeneratorFactory;
import com.onushi.springtestrecorder.codegenerator.test.TestGeneratorService;
import com.onushi.springtestrecorder.utils.ServiceCreatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ObjectInfoServiceObjectInfoChangedDeepTest {
    private ObjectInfoService objectInfoService;
    private ObjectInfo objectInfo1;
    private TestGeneratorFactory testGeneratorFactory;
    private ObjectInfoFactoryManager objectInfoFactoryManager;
    private Employee employee;

    @BeforeEach
    protected void setUp() {
        testGeneratorFactory = ServiceCreatorUtils.createTestGeneratorFactory();
        objectInfoFactoryManager = ServiceCreatorUtils.createObjectInfoFactoryManager();
        objectInfoService = new ObjectInfoService();

        ObjectInfoCreationContext objectInfoCreationContext1 = new ObjectInfoCreationContext();
        objectInfoCreationContext1.setObject(new Object());
        objectInfo1 = new ObjectInfo(objectInfoCreationContext1, "o1");

        employee = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .salaryParam1(1000)
                .salaryParam2(1500)
                .department(Department.builder()
                        .id(100)
                        .name("IT")
                        .build())
                .build();
    }

    /* TODO IB !!!! 1 reactivate
    @Test
    void objectInfoChangedDeep_BasicTest() {
        assertFalse(objectInfoService.objectInfoChangedDeep(null));
        assertFalse(objectInfoService.objectInfoChangedDeep(objectInfo1));
    }

    @Test
    void objectInfoChangedDeep_TestObjectNotChanged() {
        Employee employee = Employee.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .salaryParam1(1000)
                .salaryParam2(1500)
                .department(Department.builder()
                        .id(100)
                        .name("IT")
                        .build())
                .build();

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("getFirstName")
                .arguments(Collections.singletonList(employee))
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());

        ObjectInfo objectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator, employee);
        assertFalse(objectInfoService.objectInfoChangedDeep(objectInfo));
    }

    @Test
    void objectInfoChangedDeep_TestObjectWithChangedStringField() {
        SimplePerson simplePerson = new SimplePerson("Mary Poe");
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("someFakeMethod")
                .arguments(Collections.singletonList(simplePerson))
                .build());

        simplePerson.setName("Mary1 Poe");
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());

        ObjectInfo objectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator, simplePerson);
        assertTrue(objectInfoService.objectInfoChangedDeep(objectInfo));
    }

    @Test
    void objectInfoChangedDeep_TestObjectWithChangedObjectField() {
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("someFakeMethod")
                .arguments(Collections.singletonList(employee))
                .build());

        employee.setDepartment(Department.builder()
                .id(101)
                .name("Logistics")
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());

        ObjectInfo objectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator, employee);
        assertTrue(objectInfoService.objectInfoChangedDeep(objectInfo));
    }

    @Test
    void objectInfoChangedDeep_TestObjectWithChangedObjectField2() {
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("someFakeMethod")
                .arguments(Collections.singletonList(employee))
                .build());

        employee.setDepartment(Department.builder()
                .id(100)
                .name("IT")
                .build());
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());

        ObjectInfo objectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator, employee);
        assertTrue(objectInfoService.objectInfoChangedDeep(objectInfo));
    }

    @Test
    void objectInfoChangedDeep_TestObjectWithChangeInsideContainedObject() {
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("someFakeMethod")
                .arguments(Collections.singletonList(employee))
                .build());

        employee.getDepartment().setName("New IT");
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());

        ObjectInfo objectInfo = objectInfoFactoryManager.getCommonObjectInfo(testGenerator, employee);
        assertTrue(objectInfoService.objectInfoChangedDeep(objectInfo));
    }
    */
}
