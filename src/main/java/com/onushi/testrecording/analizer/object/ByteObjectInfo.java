package com.onushi.testrecording.analizer.object;

public class ByteObjectInfo extends ObjectInfo {
    public ByteObjectInfo(Object object, String objectName) {
        super(object, objectName);
    }

    @Override
    public String getInlineCode() {
        return "(byte)" + object;
    }

    @Override
    public boolean isOnlyInline() {
        return true;
    }
}
