package com.onushi.testrecording.analizer.object;

public class ShortObjectInfo extends ObjectInfo {
    public ShortObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return "(short)" + object;
    }

    @Override
    public boolean isInline() {
        return true;
    }
}

