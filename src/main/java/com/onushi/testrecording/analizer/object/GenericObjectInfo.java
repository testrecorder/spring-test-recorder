package com.onushi.testrecording.analizer.object;

public class GenericObjectInfo extends ObjectInfo {
    protected GenericObjectInfo(Object object, String objectName) {
        super(object, objectName, true, object.toString());
    }
}
