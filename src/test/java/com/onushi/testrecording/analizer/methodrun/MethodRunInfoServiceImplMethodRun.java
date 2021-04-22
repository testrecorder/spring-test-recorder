package com.onushi.testrecording.analizer.methodrun;

import com.onushi.testrecording.analizer.object.ObjectInfoService;
import com.onushi.testrecording.analizer.clazz.ClassService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// TODO IB how to check test coverage?
@ExtendWith(MockitoExtension.class)
class MethodRunInfoServiceImplMethodRun {
    @Mock
    MethodRunInfo methodRunInfoMock;

    @Test
    void generateObjectName() throws InterruptedException {
        Map<Object, String> objectNames = new HashMap<>();
        Map<String, Integer> lastIndexForObjectName = new HashMap<>();

        when(methodRunInfoMock.getObjectNames()).thenReturn(objectNames);
        when(methodRunInfoMock.getLastIndexForObjectName()).thenReturn(lastIndexForObjectName);

        ClassService classService = new ClassService();
        ObjectInfoService objectInfoService = new ObjectInfoService(classService);
        MethodRunInfoService methodRunInfoService = new MethodRunInfoService(classService);
        assertEquals(methodRunInfoService.generateObjectName(methodRunInfoMock, new Date()), "date1");
        Thread.sleep(1);
        assertEquals(methodRunInfoService.generateObjectName(methodRunInfoMock, new Date()), "date2");
        Thread.sleep(1);
        assertEquals(methodRunInfoService.generateObjectName(methodRunInfoMock, new Date()), "date3");
        Thread.sleep(1);
        Date sameDate = new Date();
        assertEquals(methodRunInfoService.generateObjectName(methodRunInfoMock, sameDate), "date4");
        assertEquals(methodRunInfoService.generateObjectName(methodRunInfoMock, sameDate), "date4");

        List<String> list = new ArrayList<>();
        assertEquals(methodRunInfoService.generateObjectName(methodRunInfoMock, list), "arrayList1");
        assertEquals(methodRunInfoService.generateObjectName(methodRunInfoMock, list), "arrayList1");
    }
}
