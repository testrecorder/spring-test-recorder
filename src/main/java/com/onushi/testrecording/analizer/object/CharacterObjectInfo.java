package com.onushi.testrecording.analizer.object;

public class CharacterObjectInfo extends ObjectInfo {
    public CharacterObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return "'" + object + "'";
    }

    @Override
    public boolean isInline() {
        return true;
    }
}
