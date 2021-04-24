package com.onushi.testrecording.analizer.classInfo;

import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Service
public class ClassInfoService {
    public String getFullClassName(Object object) {
        if (object == null) {
            return "null";
        } else {
            return object.getClass().getName();
        }
    }

    public String getShortClassName(Object object) {
        String fullClassName = getFullClassName(object);
        int lastPointIndex = fullClassName.lastIndexOf(".");
        if (lastPointIndex != -1) {
            return fullClassName.substring(lastPointIndex + 1);
        } else {
            return fullClassName;
        }
    }

    public String getPackageName(Object object) {
        String fullClassName = getFullClassName(object);
        int lastPointIndex = fullClassName.lastIndexOf(".");
        if (lastPointIndex != -1) {
            return fullClassName.substring(0, lastPointIndex);
        } else {
            return "";
        }
    }

    public String getObjectNameBase(Object object) {
        String getShortClassName = getShortClassName(object);
        // TODO IB !!!! extract function
        return getShortClassName.substring(0,1).toLowerCase(Locale.ROOT) + getShortClassName.substring(1);
    }

    // TODO IB compute these
    public boolean isSpringComponent() {
        return false;
    }

    public boolean canBeCreatedWithNoArgsConstructor() {
        return false;
    }

    public boolean canBeCreatedWithSetters() {
        return false;
    }

    public boolean canBeCreatedWithLombokBuilder(Object object) {
        if (object == null) {
            return false;
        } else {
            Class<?> clazz = object.getClass();
            Method[] publicMethods = clazz.getMethods();
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
                    return buildMethod.get().getReturnType() == clazz;
                }
            }
            return false;
        }
    }

}
