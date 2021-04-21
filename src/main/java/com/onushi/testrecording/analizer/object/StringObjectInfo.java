package com.onushi.testrecording.analizer.object;

public class StringObjectInfo extends ObjectInfo {
    public StringObjectInfo(Object object, String objectName) {
        super(object, objectName, true, "\"" + object + "\"");
    }
}
