package com.onushi.testrecording.generator.object;

public class GenericObjectInfo extends ObjectInfo {
    protected GenericObjectInfo(Object object, String objectName) {
        super(object, objectName, true, object.toString());
    }
}
