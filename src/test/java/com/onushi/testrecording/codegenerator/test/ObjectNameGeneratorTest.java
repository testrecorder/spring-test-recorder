package com.onushi.testrecording.codegenerator.test;

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
    TestGenerator testGeneratorMock;

    @Test
    void getObjectName() throws InterruptedException {
        Map<Object, String> objectNames = new HashMap<>();
        Map<String, Integer> lastIndexForObjectName = new HashMap<>();

        when(testGeneratorMock.getObjectNames()).thenReturn(objectNames);
        when(testGeneratorMock.getLastIndexForObjectName()).thenReturn(lastIndexForObjectName);

        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        assertEquals("date1", objectNameGenerator.getObjectName(testGeneratorMock, new Date()));
        Thread.sleep(1);
        assertEquals("date2", objectNameGenerator.getObjectName(testGeneratorMock, new Date()));
        Thread.sleep(1);
        assertEquals("date3", objectNameGenerator.getObjectName(testGeneratorMock, new Date()));
        Thread.sleep(1);
        Date sameDate = new Date();
        assertEquals("date4", objectNameGenerator.getObjectName(testGeneratorMock, sameDate));
        assertEquals("date4", objectNameGenerator.getObjectName(testGeneratorMock, sameDate));

        List<String> list = new ArrayList<>();
        assertEquals("arrayList1", objectNameGenerator.getObjectName(testGeneratorMock, list));
        assertEquals("arrayList1", objectNameGenerator.getObjectName(testGeneratorMock, list));
    }

    @Test
    void getBaseObjectName() {
        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        assertEquals("float", objectNameGenerator.getBaseObjectName(1f));
        assertEquals("null", objectNameGenerator.getBaseObjectName(null));
    }

    @Test
    void lowerCaseFirstLetter() {
        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        assertEquals("date", objectNameGenerator.lowerCaseFirstLetter("Date"));
        assertEquals("personRepository", objectNameGenerator.lowerCaseFirstLetter("PersonRepository"));
        assertEquals("d", objectNameGenerator.lowerCaseFirstLetter("D"));
        assertThrows(IllegalArgumentException.class, () -> objectNameGenerator.lowerCaseFirstLetter(""));
    }
}
