package com.onushi.testrecording.analizer.object;

public class LongObjectInfo extends ObjectInfo {
    public LongObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return object + "L";
    }

    @Override
    public boolean isInline() {
        return true;
    }
}
