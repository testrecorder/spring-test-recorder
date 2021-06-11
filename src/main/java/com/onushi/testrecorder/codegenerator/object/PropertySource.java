package com.onushi.testrecorder.codegenerator.object;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
public class PropertySource {
    private Field field;
    private Method getter;

    private PropertySource() {
    }

    public static PropertySource fromField(Field field) {
        PropertySource result = new PropertySource();
        result.field = field;
        return result;
    }

    public static PropertySource fromGetter(Method getter) {
        PropertySource result = new PropertySource();
        result.getter = getter;
        return result;
    }
}
