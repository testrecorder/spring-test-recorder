package com.onushi.testrecording.codegenerator.test;

import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

class ObjectNameGeneratorTest {

    @Test
    void getObjectName() throws InterruptedException {
        Map<String, Integer> lastIndexForObjectName = new HashMap<>();

        TestGenerator testGeneratorMock = mock(TestGenerator.class);
        when(testGeneratorMock.getLastIndexForObjectName()).thenReturn(lastIndexForObjectName);

        ObjectNameGenerator objectNameGenerator = new ObjectNameGenerator();
        assertEquals("date1", objectNameGenerator.getNewObjectName(testGeneratorMock, new Date()));
        Thread.sleep(1);
        assertEquals("date2", objectNameGenerator.getNewObjectName(testGeneratorMock, new Date()));
        Thread.sleep(1);
        assertEquals("date3", objectNameGenerator.getNewObjectName(testGeneratorMock, new Date()));
        Thread.sleep(1);
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
