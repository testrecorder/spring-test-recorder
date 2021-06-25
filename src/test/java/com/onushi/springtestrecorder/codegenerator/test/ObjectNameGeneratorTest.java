/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.springtestrecorder.utils.ServiceCreatorUtils;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjectNameGeneratorTest {

    @Test
    void getObjectName() {
        Map<String, Integer> lastIndexForObjectName = new HashMap<>();

        TestGenerator testGeneratorMock = mock(TestGenerator.class);
        when(testGeneratorMock.getLastIndexForObjectName()).thenReturn(lastIndexForObjectName);

        ObjectNameGenerator objectNameGenerator = ServiceCreatorUtils.createObjectNameGenerator();
        assertEquals("date1", objectNameGenerator.getNewObjectName(testGeneratorMock, new Date(10)));
        assertEquals("date2", objectNameGenerator.getNewObjectName(testGeneratorMock, new Date(20)));
        assertEquals("date3", objectNameGenerator.getNewObjectName(testGeneratorMock, new Date(30)));
    }

    @Test
    void getBaseObjectName() {
        ObjectNameGenerator objectNameGenerator = ServiceCreatorUtils.createObjectNameGenerator();
        assertEquals("float", objectNameGenerator.getBaseObjectName(1f));
        assertEquals("null", objectNameGenerator.getBaseObjectName(null));
    }
}
