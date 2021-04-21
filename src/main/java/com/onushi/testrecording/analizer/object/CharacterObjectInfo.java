package com.onushi.testrecording.analizer.object;

public class CharacterObjectInfo extends ObjectInfo {
    public CharacterObjectInfo(Object object, String objectName) {
        super(object, objectName, true, "'" + object + "'");
    }
}
