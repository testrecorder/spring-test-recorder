package com.onushi.testrecording.generator.object;

public class ByteObjectInfo extends ObjectInfo {
    public ByteObjectInfo(Object object, String objectName) {
        super(object, objectName, true, "(byte)" + object);
    }
}
