package com.onushi.testrecording.analizer.clazz;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// TODO IB needed?
@Getter
public class ClazzInfo {
    private final Field[] fields;
    private final Method[] methods;

    public ClazzInfo(Object object) {
        this.fields = object.getClass().getFields();
        this.methods = object.getClass().getMethods();
        try {
            object.getClass().getMethod("builder");
        } catch(Exception ignored) {
        }
    }
}
