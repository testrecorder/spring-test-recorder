package com.onushi.springtestrecorder.codegenerator.object;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyValueTest {
    protected ObjectInfo objectInfo1;
    protected ObjectInfo objectInfo2;

    @BeforeEach
    protected void setUp() {
        ObjectInfoCreationContext objectInfoCreationContext1 = new ObjectInfoCreationContext();
        objectInfoCreationContext1.setObject(new Object());
        objectInfo1 = new ObjectInfo(objectInfoCreationContext1, "o1");

        ObjectInfoCreationContext objectInfoCreationContext2 = new ObjectInfoCreationContext();
        objectInfoCreationContext2.setObject(new Object());
        objectInfo2 = new ObjectInfo(objectInfoCreationContext2, "o2");
    }

    @Test
    void isSameValue1() {
        PropertyValue propertyValue1 = PropertyValue.fromString("abc");

        PropertyValue propertyValue2 = PropertyValue.fromString("abc");
        assertTrue(propertyValue1.isSameValue(propertyValue2));
        assertTrue(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromString(null);
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(null);
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo1);
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo2);
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));
    }

    @Test
    void isSameValue2() {
        PropertyValue propertyValue1 = PropertyValue.fromString(null);

        PropertyValue propertyValue2 = PropertyValue.fromString("abc");
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromString(null);
        assertTrue(propertyValue1.isSameValue(propertyValue2));
        assertTrue(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(null);
        assertTrue(propertyValue1.isSameValue(propertyValue2));
        assertTrue(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo1);
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo2);
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));
    }

    @Test
    void isSameValue3() {
        PropertyValue propertyValue1 = PropertyValue.fromObjectInfo(objectInfo1);

        PropertyValue propertyValue2 = PropertyValue.fromString("abc");
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromString(null);
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(null);
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo1);
        assertTrue(propertyValue1.isSameValue(propertyValue2));
        assertTrue(propertyValue2.isSameValue(propertyValue1));

        propertyValue2 = PropertyValue.fromObjectInfo(objectInfo2);
        assertFalse(propertyValue1.isSameValue(propertyValue2));
        assertFalse(propertyValue2.isSameValue(propertyValue1));
    }
}
