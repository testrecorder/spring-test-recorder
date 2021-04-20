package com.onushi.testrecording.analizer.object;

public class NullObjectInfo extends ObjectInfo {
    protected NullObjectInfo() {
        super(null);
    }

    @Override
    public String getValue() {
        return "null";
    }

    @Override
    public boolean isInline() {
        return true;
    }
}
