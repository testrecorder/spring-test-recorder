package com.onushi.sampleapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SampleServiceTest {
    @Test
    void getFirstName() throws Exception {
        // Arrange
        Person person1 = Person.builder()
                .dateOfBirth(null)
                .firstName("Mary")
                .lastName("Poe")
                .build();
        SampleService sampleService = new SampleService();

        // Act
        String result = sampleService.getFirstName(person1);

        // Assert
        assertEquals("Mary", result);
    }

    @Test
    void processArrays() throws Exception {
        // Arrange
        boolean[] array1 = {true, false};
        byte[] array2 = {(byte)1, (byte)2};
        char[] array3 = {'a', 'z'};
        double[] array4 = {1.0, 2.0};
        float[] array5 = {1.0f, 2.0f};
        int[] array6 = {3, 4};
        long[] array7 = {3L, 4L};
        short[] array8 = {(short)3, (short)4};
        String[] array9 = {"a", "z"};
        Object[] array10 = {"a", 2};
        SampleService sampleService = new SampleService();

        // Act
        int result = sampleService.processArrays(array1, array2, array3, array4, array5, array6, array7, array8, array9, array10);

        // Assert
        assertEquals(42, result);
    }
}
