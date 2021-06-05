package com.onushi.sampleapp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.onushi.sampleapp.model.CyclicParent;
import java.util.List;
import java.util.Arrays;
import com.onushi.sampleapp.model.CyclicChild;

class SampleServiceTest {
    //Test Generated on 2021-06-05 13:14:59.372 with @RecordTest
    //TODO rename the test to describe the use case
    //TODO refactor the generated code to make it easier to understand
    @Test
    void processCyclicObjects() throws Exception {
        // Arrange
        CyclicChild cyclicChild1 = new CyclicChild();
        List<CyclicChild> arrayList1 = Arrays.asList(cyclicChild1);
        CyclicParent cyclicParent1 = new CyclicParent();
        cyclicParent1.childList = arrayList1;
        cyclicChild1.parent = cyclicParent1;
        SampleService sampleService = new SampleService();

        // Act
        Integer result = sampleService.processCyclicObjects(cyclicParent1);

        // Assert
        assertEquals(42, result);
    }
}
