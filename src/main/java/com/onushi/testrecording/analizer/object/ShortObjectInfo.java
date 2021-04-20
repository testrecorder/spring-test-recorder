package com.onushi.testrecording.analizer.object;

public class ShortObjectInfo extends ObjectInfo {
    public ShortObjectInfo(Object object, String objectName) {
        super(object, objectName);
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

