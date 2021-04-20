package com.onushi.testrecording.analizer.object;

public class NullObjectInfo extends ObjectInfo {
    protected NullObjectInfo() {
        super(null);
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
