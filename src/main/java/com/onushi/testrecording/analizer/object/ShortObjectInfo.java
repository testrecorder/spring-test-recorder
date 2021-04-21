package com.onushi.testrecording.analizer.object;

public class ShortObjectInfo extends ObjectInfo {
    public ShortObjectInfo(Object object, String objectName) {
        super(object, objectName, true, "(short)" + object);
    }
}

