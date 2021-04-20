package com.onushi.testrecording.analizer.object;

public class FloatObjectInfo extends ObjectInfo {
    public FloatObjectInfo(Object object) {
        super(object);
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
