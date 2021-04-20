package com.onushi.testrecording.analizer;

public class LongObjectInfo extends ObjectInfo {
    public LongObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return object + "L";
    }
}
