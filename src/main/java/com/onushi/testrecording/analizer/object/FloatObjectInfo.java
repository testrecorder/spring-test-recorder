package com.onushi.testrecording.analizer.object;

public class FloatObjectInfo extends ObjectInfo {
    public FloatObjectInfo(Object object, String objectName) {
        super(object, objectName);
    }

    @Override
    public String getInlineCode() {
        return object + "f";
    }

    @Override
    public boolean isOnlyInline() {
        return true;
    }
}
