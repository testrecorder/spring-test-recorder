package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;

class SampleServiceTest {
    @Test
    void repeatedArgs() throws Exception {
        // Arrange
        int[] array1 = {3, 4, 3};
        List<Float> arrayList1 = Arrays.asList(3.0f, 3.0f);
        SampleService sampleService = new SampleService();

        // Act
        Integer result = sampleService.repeatedArgs(array1, arrayList1);

        // Assert
        assertEquals(42, result);
    }
}
