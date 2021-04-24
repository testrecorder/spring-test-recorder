package com.onushi.testrecording.codegenerator;

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
class MethodRunInfoServiceImplMethodRun {
    @Mock
    TestGenInfo testGenInfoMock;

    @Test
    void generateObjectName() throws InterruptedException {
        Map<Object, String> objectNames = new HashMap<>();
        Map<String, Integer> lastIndexForObjectName = new HashMap<>();

        when(testGenInfoMock.getObjectNames()).thenReturn(objectNames);
        when(testGenInfoMock.getLastIndexForObjectName()).thenReturn(lastIndexForObjectName);

        ClassInfoService classInfoService = new ClassInfoService();
        ObjectNamesService objectNamesService = new ObjectNamesService(classInfoService);
        assertEquals(objectNamesService.generateObjectName(testGenInfoMock, new Date()), "date1");
        Thread.sleep(1);
        assertEquals(objectNamesService.generateObjectName(testGenInfoMock, new Date()), "date2");
        Thread.sleep(1);
        assertEquals(objectNamesService.generateObjectName(testGenInfoMock, new Date()), "date3");
        Thread.sleep(1);
        Date sameDate = new Date();
        assertEquals(objectNamesService.generateObjectName(testGenInfoMock, sameDate), "date4");
        assertEquals(objectNamesService.generateObjectName(testGenInfoMock, sameDate), "date4");

        List<String> list = new ArrayList<>();
        assertEquals(objectNamesService.generateObjectName(testGenInfoMock, list), "arrayList1");
        assertEquals(objectNamesService.generateObjectName(testGenInfoMock, list), "arrayList1");
    }
}
