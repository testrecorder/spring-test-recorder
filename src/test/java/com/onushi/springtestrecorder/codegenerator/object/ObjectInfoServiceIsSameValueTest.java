package com.onushi.springtestrecorder.codegenerator.object;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectInfoServiceIsSameValueTest {
    private ObjectInfoService objectInfoService;
    private ObjectInfo objectInfo1;
    private ObjectInfo objectInfo2;

    @BeforeEach
    protected void setUp() {
        objectInfoService = new ObjectInfoService();

        ObjectInfoCreationContext objectInfoCreationContext1 = new ObjectInfoCreationContext();
        objectInfoCreationContext1.setObject(new Object());
        objectInfo1 = new ObjectInfo(objectInfoCreationContext1, "o1");

        ObjectInfoCreationContext objectInfoCreationContext2 = new ObjectInfoCreationContext();
        objectInfoCreationContext2.setObject(new Object());
        objectInfo2 = new ObjectInfo(objectInfoCreationContext2, "o2");
    }

    /* TODO IB !!!! 1 reactivate
    @Test
    void isSameValue1() {
        PropertyValue propertyValue1 = PropertyValue.fromString("abc");

        PropertyValue propertyValue2 = PropertyValue.fromString("abc");
        assertTrue(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertTrue(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromString(null);
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(null);
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo1);
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo2);
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));
    }

    @Test
    void isSameValue2() {
        PropertyValue propertyValue1 = PropertyValue.fromString(null);

        PropertyValue propertyValue2 = PropertyValue.fromString("abc");
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromString(null);
        assertTrue(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertTrue(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(null);
        assertTrue(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertTrue(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo1);
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo2);
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));
    }

    @Test
    void isSameValue3() {
        PropertyValue propertyValue1 = PropertyValue.fromObjectInfo(objectInfo1);

        PropertyValue propertyValue2 = PropertyValue.fromString("abc");
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromString(null);
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(null);
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo1);
        assertTrue(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertTrue(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo2);
        assertFalse(objectInfoService.isSameValueDeep(propertyValue1, propertyValue2));
        assertFalse(objectInfoService.isSameValueDeep(propertyValue2, propertyValue1));
    }
     */
}
