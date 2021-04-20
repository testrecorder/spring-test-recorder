package com.onushi.testrecording.analizer;

public class NullObjectInfo extends ObjectInfo {
    protected NullObjectInfo() {
        super(null);
    }

    @Override
    public String getValue() {
        return "null";
    }
}
