package com.onushi.testrecording.analizer;

public class StringObjectInfo extends ObjectInfo {
    public StringObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        return "\"" + object + "\"";
    }
}
