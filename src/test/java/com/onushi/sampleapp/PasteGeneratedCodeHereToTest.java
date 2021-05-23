package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;;

class SampleServiceTest {
    @Test
    void processMap() throws Exception {
        // Arrange
        List<String> arrayList1 = Arrays.asList("0", "1");
        List<String> arrayList2 = Arrays.asList("0", "1", "2");
        List<String> arrayList3 = Arrays.asList("0", "1", "2", "3");
        Map<String, List<String>> hashMap1 = new HashMap<>();
        hashMap1.put("1", arrayList1);
        hashMap1.put("2", arrayList2);
        hashMap1.put("3", arrayList3);
        SampleService sampleService = new SampleService();

        // Act
        int result = sampleService.processMap(hashMap1);

        // Assert
        assertEquals(42, result);
    }
}
