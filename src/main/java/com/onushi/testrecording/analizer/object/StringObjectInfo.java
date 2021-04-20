package com.onushi.testrecording.analizer.object;

public class StringObjectInfo extends ObjectInfo {
    public StringObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getInlineCode() {
        return "\"" + object + "\"";
    }

    @Override
    public boolean isOnlyInline() {
        return true;
    }
}
