package com.onushi.testrecording.analizer.object;

public class FloatObjectInfo extends ObjectInfo {
    public FloatObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return object + "f";
    }
}
