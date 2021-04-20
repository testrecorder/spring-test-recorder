package com.onushi.testrecording.analizer.object;

public class ByteObjectInfo extends ObjectInfo {
    public ByteObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return "(byte)" + object;
    }
}
