package com.onushi.testrecording.analizer.classInfo;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// TODO IB check object fields, setters, constructors etc
@Getter
public class ClassInfo {
    private final Field[] fields;
    private final Method[] methods;
    private final boolean hasLombokBuilder;

    public ClassInfo(Object object) {
        this.fields = object.getClass().getFields();
        this.methods = object.getClass().getMethods();
        this.hasLombokBuilder = false;
        try {
            object.getClass().getMethod("builder");
        } catch(Exception ignored) {
        }
    }
}
