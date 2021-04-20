package com.onushi.testrecording.analizer.object;

public class GenericObjectInfo extends ObjectInfo {
    protected GenericObjectInfo(Object object) {
        super(object);
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
