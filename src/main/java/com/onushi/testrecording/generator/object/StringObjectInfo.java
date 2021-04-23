package com.onushi.testrecording.generator.object;

public class StringObjectInfo extends ObjectInfo {
    public StringObjectInfo(Object object, String objectName) {
        super(object, objectName, true, "\"" + object + "\"");
    }
}
