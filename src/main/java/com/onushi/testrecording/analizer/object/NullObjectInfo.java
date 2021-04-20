package com.onushi.testrecording.analizer.object;

public class NullObjectInfo extends ObjectInfo {
    protected NullObjectInfo(String objectName) {
        super(null, objectName);
    }

    @Override
    public String getInlineCode() {
        return "null";
    }

    @Override
    public boolean isOnlyInline() {
        return true;
    }
}
