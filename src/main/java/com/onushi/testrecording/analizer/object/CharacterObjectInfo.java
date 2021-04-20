package com.onushi.testrecording.analizer.object;

public class CharacterObjectInfo extends ObjectInfo {
    public CharacterObjectInfo(Object object) {
        super(object);
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
