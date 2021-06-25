/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

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

    @Override
    public String toString() {
        return "PropertyValue{" +
                "string='" + string + '\'' +
                ", objectInfo=" + objectInfo +
                '}';
    }
}
