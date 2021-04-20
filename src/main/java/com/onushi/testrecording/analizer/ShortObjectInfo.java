package com.onushi.testrecording.analizer;

public class ShortObjectInfo extends ObjectInfo {
    public ShortObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return "(short)" + object;
    }
}

