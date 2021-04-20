package com.onushi.testrecording.analizer.object;

public class GenericObjectInfo extends ObjectInfo {
    protected GenericObjectInfo(Object object, String objectName) {
        super(object, objectName);
    }

    @Override
    public String getInlineCode() {
        return object.toString();
    }

    @Override
    public boolean isOnlyInline() {
        return true;
    }
}
