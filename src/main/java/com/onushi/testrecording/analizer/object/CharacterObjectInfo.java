package com.onushi.testrecording.analizer.object;

public class CharacterObjectInfo extends ObjectInfo {
    public CharacterObjectInfo(Object object, String objectName) {
        super(object, objectName);
    }

    @Override
    public String getInlineCode() {
        return "'" + object + "'";
    }

    @Override
    public boolean isOnlyInline() {
        return true;
    }
}
