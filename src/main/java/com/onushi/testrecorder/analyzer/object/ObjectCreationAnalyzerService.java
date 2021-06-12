package com.onushi.testrecorder.analyzer.object;

import com.onushi.testrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecorder.analyzer.classInfo.MatchingConstructor;
import com.onushi.testrecorder.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObjectCreationAnalyzerService {
    private final StringService stringService;
    private final ClassInfoService classInfoService;

    public ObjectCreationAnalyzerService(StringService stringService,
                                         ClassInfoService classInfoService) {
        this.stringService = stringService;
        this.classInfoService = classInfoService;
    }

    public boolean canBeCreatedWithLombokBuilder(Object object) {
        if (object == null) {
            return false;
        } else {
            return classInfoService.canBeCreatedWithLombokBuilder(object.getClass());
        }
    }

    public boolean canBeCreatedWithNoArgsConstructor(Object object, Map<String, FieldValue> objectState) {
        if (object == null) {
            return false;
        }
        if (!classInfoService.hasPublicNoArgsConstructor(object.getClass())) {
            return false;
        }

        long valuesDifferentThanDefaults = 0;
        for (FieldValue fieldValue : objectState.values()) {
            if (fieldValue.getFieldValueStatus() == FieldValueStatus.COULD_NOT_READ) {
                return false;
            }
            if (fieldValue.getFieldValueStatus() == FieldValueStatus.VALUE_READ) {
                if (isDefaultValueForItsClass(fieldValue.getValue())) {
                    continue;
                }
            }

            valuesDifferentThanDefaults++;
        }
        return valuesDifferentThanDefaults == 0;
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

    public List<MatchingConstructor> getMatchingAllArgsConstructors(Object object, Map<String, FieldValue> objectState) {
        if (object == null) {
            return new ArrayList<>();
        }
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

            orderOfFields.add(matchingFields.get(0));
            fieldsToMatch.remove(matchingFields.get(0));
        }
        if (fieldsToMatch.size() == 0) {
            return Collections.singletonList(MatchingConstructor.builder()
                    .constructor(constructor)
                    .argsInOrder(orderOfFields)
                    .fieldsCouldHaveDifferentOrder(fieldsCouldHaveDifferentOrder)
                    .build());
        } else {
            return new ArrayList<>();
        }
    }

    public Map<String, SetterInfo> getSettersForFields(Object object, Map<String, FieldValue> fields) {
        // TODO IB I should use @NotNull from lombok instead
        if (object == null) {
            return new HashMap<>();
        }
        List<Method> possibleSetters = Arrays.stream(object.getClass()
                .getMethods())
                // TODO IB !!!! everywhere where we allow Public, allow also package and protected for isSamePackage
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                // TODO IB sometimes setters don't start with set
                .filter(method -> method.getName().startsWith("set"))
                .filter(method -> method.getParameterTypes().length == 1)
                .collect(Collectors.toList());

        Map<String, SetterInfo> result = new HashMap<>();
        for (Map.Entry<String, FieldValue> entry : fields.entrySet()) {
            String setterName = getSetterName(entry.getValue().getField());
            Method setter = possibleSetters.stream().filter(x -> x.getName().equals(setterName)).findFirst().orElse(null);
            if (setter != null) {
                SetterInfo setterInfo = SetterInfo.builder()
                        .name(setterName)
                        .setter(setter)
                        .isForBuilder(setter.getReturnType() == object.getClass())
                        .build();

                result.put(entry.getKey(), setterInfo);
            }
        }

        return result;
    }

    private String getSetterName(Field field) {
        String fieldName = field.getName();
        if ((field.getType() == boolean.class || field.getType() == Boolean.class) &&
                (fieldName.matches("^is[A-Z].*$"))) {
            return "set" + fieldName.substring(2);
        }
        return "set" + stringService.upperCaseFirstLetter(fieldName);
    }

    public boolean canBeCreatedWithNoArgsAndFields(Object object, Map<String, FieldValue> objectState,
                   boolean allowPackageAndProtected) {
        if (object == null) {
            return false;
        }
        if (!classInfoService.hasPublicNoArgsConstructor(object.getClass())) {
            return false;
        }
        for (FieldValue x : objectState.values()) {
            if (x.getFieldValueStatus() != FieldValueStatus.VALUE_READ) {
                return false;
            }
            if (allowPackageAndProtected) {
                if (Modifier.isPrivate(x.getField().getModifiers())) {
                    return false;
                }
            } else {
                if (!Modifier.isPublic(x.getField().getModifiers())) {
                    return false;
                }
            }
        }
        return true;
    }
}
