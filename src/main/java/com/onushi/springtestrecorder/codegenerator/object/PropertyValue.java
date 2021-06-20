package com.onushi.springtestrecorder.codegenerator.object;

import lombok.Getter;

@Getter
public class PropertyValue {
    private String string = null;
    private ObjectInfo objectInfo = null;

    private PropertyValue() {}

    public static PropertyValue fromString(String string) {
        PropertyValue result = new PropertyValue();
        result.string = string;
        return result;
    }

    public static PropertyValue fromObjectInfo(ObjectInfo objectInfo) {
        PropertyValue result = new PropertyValue();
        result.objectInfo = objectInfo;
        return result;
    }

    public boolean isSameValue(PropertyValue otherPropertyValue) {
        if (otherPropertyValue == null) {
            return false;
        }
        if (this.string != null) {
            return this.string.equals(otherPropertyValue.getString());
        } else if (this.objectInfo != null) {
            // TODO IB !!!! here check recursively for changes
            return this.objectInfo == otherPropertyValue.objectInfo;
        }
        return otherPropertyValue.string == null && otherPropertyValue.objectInfo == null;
    }
}
