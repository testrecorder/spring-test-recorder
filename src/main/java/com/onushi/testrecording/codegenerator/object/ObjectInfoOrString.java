package com.onushi.testrecording.codegenerator.object;

import lombok.Getter;

// TODO IB !!!! rename to PropertyValue
@Getter
public class ObjectInfoOrString {
    private String string = null;
    private ObjectInfo objectInfo = null;

    private ObjectInfoOrString() {}

    public static ObjectInfoOrString fromString(String string) {
        ObjectInfoOrString result = new ObjectInfoOrString();
        result.string = string;
        return result;
    }

    public static ObjectInfoOrString fromObjectInfo(ObjectInfo objectInfo) {
        ObjectInfoOrString result = new ObjectInfoOrString();
        result.objectInfo = objectInfo;
        return result;
    }
}
