package com.onushi.testrecording.analizer.classInfo;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

@Getter
public class ClassInfo {
    private final Field[] publicFields;
    private final Method[] publicMethods;

    public ClassInfo(Class<?> clazz) {
        publicFields = clazz.getFields();
        publicMethods = clazz.getMethods();
        try {
            clazz.getMethod("builder");
        } catch(Exception ignored) {
        }
    }

    // TODO IB !!!! compute these
    public boolean isSpringComponent() {
        return false;
    }

    public boolean canBeCreatedWithNoArgsConstructor() {
        return false;
    }

    public boolean canBeCreatedWithLombokBuilder() {
        Optional<Method> builder = Arrays.stream(publicMethods)
                .filter(method -> method.getName().equals("builder") &&
                        Modifier.isStatic(method.getModifiers()))
                .findFirst();
        if (builder.isPresent()) {
            return true;
        }
        return false;
    }

    public boolean canBeCreatedWithSetters() {
        return false;
    }
}
