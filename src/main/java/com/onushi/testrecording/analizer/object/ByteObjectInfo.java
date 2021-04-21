package com.onushi.testrecording.analizer.object;

public class ByteObjectInfo extends ObjectInfo {
    public ByteObjectInfo(Object object, String objectName) {
        super(object, objectName, true, "(byte)" + object);
    }
}
