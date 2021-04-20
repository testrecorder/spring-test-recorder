package com.onushi.testrecording.analizer;

public class GenericObjectInfo extends ObjectInfo {
    protected GenericObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return object.toString();
    }
}
