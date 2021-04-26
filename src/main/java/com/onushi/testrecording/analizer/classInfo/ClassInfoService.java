package com.onushi.testrecording.analizer.classInfo;

import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassInfoService {
    public boolean isSpringComponent(Class<?> clazz) {
        return Arrays.stream(clazz.getAnnotations()).anyMatch(x ->
                x.annotationType().getName().equals("org.springframework.stereotype.Component") ||
                x.annotationType().getName().equals("org.springframework.stereotype.Service") ||
                x.annotationType().getName().equals("org.springframework.stereotype.Repository") ||
                x.annotationType().getName().equals("org.springframework.stereotype.Controller"));
    }

    public boolean hasEquals(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods()).anyMatch(method -> method.getName().equals("equals") &&
                method.getDeclaringClass() == clazz);
    }

    // TODO IB compute these
    public boolean canBeCreatedWithNoArgsConstructor() {
        return false;
    }

    public boolean canBeCreatedWithSetters() {
        return false;
    }

    public boolean canBeCreatedWithLombokBuilder(Class<?> clazz) {
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

    // TODO IB !!!! should work on classes
    public List<Method> getLombokBuilderSetters(Object object) {
        List<Method> result = new ArrayList<>();
        if (object != null) {
            Class<?> clazz = object.getClass();
            Method[] publicMethods = clazz.getMethods();
            Optional<Method> builderMethod = Arrays.stream(publicMethods)
                    .filter(method -> method.getName().equals("builder") &&
                            Modifier.isStatic(method.getModifiers()))
                    .findFirst();
            if (builderMethod.isPresent()) {
                Class<?> builderClass = builderMethod.get().getReturnType();
                return Arrays.stream(builderClass.getMethods())
                        .filter(method -> method.getReturnType() == builderClass)
                        .sorted(Comparator.comparing(Method::getName))
                        .collect(Collectors.toList());
            }
        }
        return result;
    }
}
