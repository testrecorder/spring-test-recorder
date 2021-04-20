package com.onushi.testrecording.analizer;

public class CharacterObjectInfo extends ObjectInfo {
    public CharacterObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return "'" + object + "'";
    }
}
