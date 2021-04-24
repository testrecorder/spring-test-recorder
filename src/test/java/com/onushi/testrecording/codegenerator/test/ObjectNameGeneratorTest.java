package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// TODO IB how to check test coverage?
@ExtendWith(MockitoExtension.class)
class ObjectNameGeneratorTest {
    @Mock
    TestGenenerator testGeneneratorMock;

    @Test
    void generateObjectName() throws InterruptedException {
        Map<Object, String> objectNames = new HashMap<>();
        Map<String, Integer> lastIndexForObjectName = new HashMap<>();

        when(testGeneneratorMock.getObjectNames()).thenReturn(objectNames);
        when(testGeneneratorMock.getLastIndexForObjectName()).thenReturn(lastIndexForObjectName);

        ClassInfoService classInfoService = new ClassInfoService();
        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator(classInfoService);
        assertEquals(objectNameGenerator.generateObjectName(testGeneneratorMock, new Date()), "date1");
        Thread.sleep(1);
        assertEquals(objectNameGenerator.generateObjectName(testGeneneratorMock, new Date()), "date2");
        Thread.sleep(1);
        assertEquals(objectNameGenerator.generateObjectName(testGeneneratorMock, new Date()), "date3");
        Thread.sleep(1);
        Date sameDate = new Date();
        assertEquals(objectNameGenerator.generateObjectName(testGeneneratorMock, sameDate), "date4");
        assertEquals(objectNameGenerator.generateObjectName(testGeneneratorMock, sameDate), "date4");

        List<String> list = new ArrayList<>();
        assertEquals(objectNameGenerator.generateObjectName(testGeneneratorMock, list), "arrayList1");
        assertEquals(objectNameGenerator.generateObjectName(testGeneneratorMock, list), "arrayList1");
    }
}