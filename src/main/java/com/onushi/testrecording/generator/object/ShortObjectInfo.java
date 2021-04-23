package com.onushi.testrecording.generator.object;

public class ShortObjectInfo extends ObjectInfo {
    public ShortObjectInfo(Object object, String objectName) {
        super(object, objectName, true, "(short)" + object);
    }
}

