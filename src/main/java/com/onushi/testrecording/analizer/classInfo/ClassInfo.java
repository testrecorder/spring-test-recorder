package com.onushi.testrecording.analizer.classInfo;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// TODO IB create a class analyzer to check object fields, setters and constructors
// TODO IB needed?
@Getter
public class ClassInfo {
    private final Field[] fields;
    private final Method[] methods;

    public ClassInfo(Object object) {
        this.fields = object.getClass().getFields();
        this.methods = object.getClass().getMethods();
        try {
            object.getClass().getMethod("builder");
        } catch(Exception ignored) {
        }
    }
}
