package com.onushi.testrecording.analizer.object;

public class LongObjectInfo extends ObjectInfo {
    public LongObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getInlineCode() {
        return object + "L";
    }

    @Override
    public boolean isOnlyInline() {
        return true;
    }
}
