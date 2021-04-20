package com.onushi.testrecording.analizer.object;

public class LongObjectInfo extends ObjectInfo {
    public LongObjectInfo(Object object, String objectName) {
        super(object, objectName);
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
