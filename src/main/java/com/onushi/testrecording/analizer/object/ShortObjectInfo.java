package com.onushi.testrecording.analizer.object;

public class ShortObjectInfo extends ObjectInfo {
    public ShortObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getInlineCode() {
        return "(short)" + object;
    }

    @Override
    public boolean isOnlyInline() {
        return true;
    }
}

