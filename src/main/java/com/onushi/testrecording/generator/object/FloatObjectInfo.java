package com.onushi.testrecording.generator.object;

public class FloatObjectInfo extends ObjectInfo {
    public FloatObjectInfo(Object object, String objectName) {
        super(object, objectName, true, object + "f");
    }
}
