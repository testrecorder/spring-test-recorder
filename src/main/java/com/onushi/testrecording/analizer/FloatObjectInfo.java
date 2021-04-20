package com.onushi.testrecording.analizer;

public class FloatObjectInfo extends ObjectInfo {
    public FloatObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return object + "f";
    }
}
