package com.onushi.testrecording.analizer;

import com.onushi.testrecording.analizer.test.ObjectNameGeneratorImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// TODO IB how to check test coverage?
class ObjectNameGeneratorImplTest {

    @Test
    void generateObjectName() throws InterruptedException {
        ObjectNameGeneratorImpl objectNameGenerator = new ObjectNameGeneratorImpl();
        assertEquals(objectNameGenerator.generateObjectName(new Date()), "date1");
        Thread.sleep(1);
        assertEquals(objectNameGenerator.generateObjectName(new Date()), "date2");
        Thread.sleep(1);
        assertEquals(objectNameGenerator.generateObjectName(new Date()), "date3");
        Thread.sleep(1);
        Date sameDate = new Date();
        assertEquals(objectNameGenerator.generateObjectName(sameDate), "date4");
        assertEquals(objectNameGenerator.generateObjectName(sameDate), "date4");

        List<String> list = new ArrayList<>();
        assertEquals(objectNameGenerator.generateObjectName(list), "arrayList1");
        assertEquals(objectNameGenerator.generateObjectName(list), "arrayList1");
    }
}
