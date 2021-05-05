package com.onushi.testrecording.analyzer.classInfo;

import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.analyzer.object.FieldValueType;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
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

    public List<Constructor<?>> getPublicConstructors(Class<?> clazz) {
        return Arrays.stream(clazz.getConstructors())
                .filter(constructor -> Modifier.isPublic(constructor.getModifiers()))
                .collect(Collectors.toList());
    }

    public boolean hasPublicNoArgsConstructor(Class<?> clazz) {
        List<Constructor<?>> publicConstructors = getPublicConstructors(clazz);
        return publicConstructors.stream()
                .anyMatch(x -> x.getParameterTypes().length == 0);
    }

    public boolean hasEquals(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .anyMatch(method -> method.getName().equals("equals") && method.getDeclaringClass() == clazz);
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

    // TODO IB !!!! move to ObjectCreationAnalyzerService
    public List<MatchingConstructor> getMatchingConstructorsWithAllFields(Class<?> clazz, Map<String, FieldValue> fieldValuesMap) {
        if (fieldValuesMap == null) {
            return new ArrayList<>();
        }
        Collection<FieldValue> fieldValues = fieldValuesMap.values();
        if (fieldValues.stream().anyMatch(x -> x.getFieldValueType() == FieldValueType.COULD_NOT_READ)) {
            return new ArrayList<>();
        }

        List<Constructor<?>> publicConstructorsWithCorrectSize = getPublicConstructors(clazz)
                .stream()
                .filter(x -> x.getParameterTypes().length == fieldValues.size())
                .collect(Collectors.toList());

        List<MatchingConstructor> matchingConstructors = new ArrayList<>();
        for (Constructor<?> constructor : publicConstructorsWithCorrectSize) {
            matchingConstructors.addAll(getMatchingConstructorsWithAllFields(constructor, fieldValues));
        }
        return matchingConstructors;
    }

    // TODO this algorithm can be improved to produce better matches
    private List<MatchingConstructor> getMatchingConstructorsWithAllFields(Constructor<?> constructor, Collection<FieldValue> fieldValues) {
        boolean fieldsCouldHaveDifferentOrder = false;
        List<FieldValue> orderOfFields = new ArrayList<>();
        List<FieldValue> fieldsToMatch = new ArrayList<>(fieldValues);
        for (Class<?> constructorParameterType : constructor.getParameterTypes()) {
            List<FieldValue> matchingFields = fieldsToMatch.stream()
                    .filter(x -> constructorParameterType.isAssignableFrom(x.getClazz()))
                    .collect(Collectors.toList());
            if (matchingFields.size() == 0) {
                break;
            }
            if (matchingFields.size() > 1) {
                fieldsCouldHaveDifferentOrder = true;
            }
            if (matchingFields.size() > 0) {
                orderOfFields.add(matchingFields.get(0));
                fieldsToMatch.remove(matchingFields.get(0));
            }
        }
        if (fieldsToMatch.size() == 0) {
            return Collections.singletonList(MatchingConstructor.builder()
                    .constructor(constructor)
                    .orderOfFields(orderOfFields)
                    .fieldsCouldHaveDifferentOrder(fieldsCouldHaveDifferentOrder)
                    .build());
        } else {
            return new ArrayList<>();
        }
    }



}
