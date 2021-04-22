package com.onushi.testrecording.analizer;

import com.onushi.testrecording.analizer.test.ObjectNameService;
import com.onushi.testrecording.analizer.test.ObjectNameServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// TODO IB how to check test coverage?
class ObjectNameServiceImplTest {

    @Test
    void generateObjectName() throws InterruptedException {
        ObjectNameService objectNameService = new ObjectNameServiceImpl();
        assertEquals(objectNameService.generateObjectName(new Date()), "date1");
        Thread.sleep(1);
        assertEquals(objectNameService.generateObjectName(new Date()), "date2");
        Thread.sleep(1);
        assertEquals(objectNameService.generateObjectName(new Date()), "date3");
        Thread.sleep(1);
        Date sameDate = new Date();
        assertEquals(objectNameService.generateObjectName(sameDate), "date4");
        assertEquals(objectNameService.generateObjectName(sameDate), "date4");

        List<String> list = new ArrayList<>();
        assertEquals(objectNameService.generateObjectName(list), "arrayList1");
        assertEquals(objectNameService.generateObjectName(list), "arrayList1");
    }
}
