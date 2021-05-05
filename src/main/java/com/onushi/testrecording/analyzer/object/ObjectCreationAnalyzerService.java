package com.onushi.testrecording.analyzer.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.classInfo.MatchingConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObjectCreationAnalyzerService {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;

    public ObjectCreationAnalyzerService(ClassInfoService classInfoService, ObjectStateReaderService objectStateReaderService) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
    }

    public boolean canBeCreatedWithLombokBuilder(Object object) {
        if (object == null) {
            return false;
        } else {
            return classInfoService.canBeCreatedWithLombokBuilder(object.getClass());
        }
    }

    public boolean canBeCreatedWithNoArgsConstructor(Object object) {
        if (object == null) {
            return false;
        } else {
            Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(object);

            long valuesDifferentThanDefaults = 0;
            for (FieldValue fieldValue : objectState.values()) {
                if (fieldValue.getFieldValueStatus() == FieldValueStatus.COULD_NOT_READ) {
                    continue;
                }
                if (fieldValue.getFieldValueStatus() == FieldValueStatus.VALUE_READ) {
                    if (isDefaultValueForItsClass(fieldValue.getValue())) {
                        continue;
                    }
                }

                valuesDifferentThanDefaults++;
            }
            return classInfoService.hasPublicNoArgsConstructor(object.getClass()) &&
                    valuesDifferentThanDefaults == 0;
        }
    }

    public boolean isDefaultValueForItsClass(Object object) {
        if (object == null) {
            return true;
        }

        String fullClassName = object.getClass().getName();
        switch (fullClassName) {
            case "java.lang.Boolean":
                return Boolean.FALSE.equals(object);
            case "java.lang.Byte":
            case "java.lang.Short":
            case "java.lang.Integer":
            case "java.lang.Long":
            case "java.lang.Float":
            case "java.lang.Double":
                return (((Number) object)).doubleValue() == 0;
            case "java.lang.Character":
                return (Character) object == 0;
            default:
                return false;
        }
    }

    public List<MatchingConstructor> getMatchingAllArgsConstructors(Object object) {
        if (object == null) {
            return new ArrayList<>();
        }
        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(object);
        Class<?> clazz = object.getClass();
        if (objectState == null) {
            return new ArrayList<>();
        }
        Collection<FieldValue> fieldValues = objectState.values();
        if (fieldValues.stream().anyMatch(x -> x.getFieldValueStatus() == FieldValueStatus.COULD_NOT_READ)) {
            return new ArrayList<>();
        }

        List<Constructor<?>> publicConstructorsWithCorrectSize = classInfoService.getPublicConstructors(clazz)
                .stream()
                .filter(x -> x.getParameterTypes().length == fieldValues.size())
                .collect(Collectors.toList());

        List<MatchingConstructor> matchingConstructors = new ArrayList<>();
        for (Constructor<?> constructor : publicConstructorsWithCorrectSize) {
            matchingConstructors.addAll(getMatchingAllArgsConstructors(constructor, fieldValues));
        }
        return matchingConstructors;
    }

    // TODO this algorithm can be improved to produce better matches
    private List<MatchingConstructor> getMatchingAllArgsConstructors(Constructor<?> constructor, Collection<FieldValue> fieldValues) {
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

    // TODO IB !!!! add more here
}
