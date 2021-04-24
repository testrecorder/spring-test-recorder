package com.onushi.testrecording.analizer.classInfo;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

@Getter
public class ClassInfo {
    private final Class<?> clazz;
    private final Field[] publicFields;
    private final Method[] publicMethods;

    public ClassInfo(Class<?> clazz) {
        this.clazz = clazz;
        publicFields = clazz.getFields();
        publicMethods = clazz.getMethods();
    }

    // TODO IB !!!! compute these
    public boolean isSpringComponent() {
        return false;
    }

    public boolean canBeCreatedWithNoArgsConstructor() {
        return false;
    }

    public boolean canBeCreatedWithLombokBuilder() {
        Optional<Method> builderMethod = Arrays.stream(publicMethods)
                .filter(method -> method.getName().equals("builder") &&
                        Modifier.isStatic(method.getModifiers()))
                .findFirst();
        if (builderMethod.isPresent()) {
            Class<?> builderClass = builderMethod.get().getReturnType();
            Optional<Method> buildMethod = Arrays.stream(builderClass.getMethods())
                    .filter(method -> method.getName().equals("build"))
                    .findFirst();
            if (buildMethod.isPresent()) {
                return buildMethod.get().getReturnType() == this.clazz;
            }
        }
        return false;
    }

    public boolean canBeCreatedWithSetters() {
        return false;
    }
}
