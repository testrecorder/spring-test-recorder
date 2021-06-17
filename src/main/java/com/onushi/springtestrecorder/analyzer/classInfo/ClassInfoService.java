package com.onushi.springtestrecorder.analyzer.classInfo;

import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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

    public List<Constructor<?>> getAccessibleConstructors(Class<?> clazz, boolean allowPackageAndProtected) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor ->
                        allowPackageAndProtected ? (!Modifier.isPrivate(constructor.getModifiers())) : Modifier.isPublic(constructor.getModifiers()))
                .collect(Collectors.toList());
    }

    public boolean hasAccessibleNoArgsConstructor(Class<?> clazz, boolean allowPackageAndProtected) {
        List<Constructor<?>> allConstructors = Arrays.stream(clazz.getDeclaredConstructors())
                .collect(Collectors.toList());
        if (allConstructors.size() == 0) {
            return true;
        } else {
            if (allowPackageAndProtected) {
                return allConstructors.stream()
                        .filter(constructor -> !Modifier.isPrivate(constructor.getModifiers()))
                        .anyMatch(x -> x.getParameterTypes().length == 0);
            } else {
                return allConstructors.stream()
                        .filter(constructor -> Modifier.isPublic(constructor.getModifiers()))
                        .anyMatch(x -> x.getParameterTypes().length == 0);
            }
        }
    }

    public boolean hasEquals(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .anyMatch(method -> method.getName().equals("equals") && method.getDeclaringClass() == clazz);
    }

    public boolean canBeCreatedWithLombokBuilder(Class<?> clazz) {
        Optional<Method> builderMethod = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().equals("builder") &&
                        Modifier.isPublic(method.getModifiers()) &&
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

    public List<Method> getLombokBuilderSetters(Class<?> clazz) {
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
        return new ArrayList<>();
    }

    public List<Method> getPublicGetters(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getParameterTypes().length == 0)
                .filter(method -> (method.getName().startsWith("get") ||
                        // TODO IB should be followed by big letter
                        method.getName().startsWith("is")) &&
                        !method.getName().equals("getClass"))
                .sorted(Comparator.comparing(Method::getName))
                .collect(Collectors.toList());
    }

    public List<Field> getPublicFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .filter(field -> !Modifier.isTransient(field.getModifiers()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }
}
