/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.analyzer.object;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObjectStateReaderService {
    /**
     * @param object The source object
     * @return A map of all the fields and info about them
     */
    public Map<String, FieldValue> getObjectState(Object object) {
        if (object == null) {
            return new HashMap<>();
        }
        Map<String, FieldValue> result = new HashMap<>();
        Class<?> clazz = object.getClass();
        do {
            result.putAll(addFieldsForClass(object, clazz));
            clazz = clazz.getSuperclass();
        } while(clazz != null);
        return result;
    }

    private Map<String, FieldValue> addFieldsForClass(Object object, Class<?> clazz) {
        Map<String, FieldValue> result = new HashMap<>();

        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !Modifier.isTransient(field.getModifiers()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());
        for (Field field : fields) {
            String fieldName = field.getName();
            try {
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                result.put(fieldName, new FieldValue(field.get(object), field.getType(), FieldValueStatus.VALUE_READ, field));
            } catch (Exception ignored) {
                result.put(fieldName, new FieldValue(null, null, FieldValueStatus.COULD_NOT_READ, field));
            }
        }
        return result;
    }
}

//    private void getFromPublicGetters(Object object, Map<String, Optional<Object>> result) {
//        List<Method> getters = Arrays.stream(object.getClass().getMethods())
//                .filter(method -> Modifier.isPublic(method.getModifiers()))
//                .filter(method -> method.getParameterTypes().length == 0)
//                .filter(method -> method.getName().startsWith("get") && !method.getName().equals("getClass"))
//                .collect(Collectors.toList());
//        for (Method getter: getters) {
//            String fieldName = getter.getName().substring(3);
//            fieldName = objectNameGenerator.lowerCaseFirstLetter(fieldName);
//            try {
//                result.put(fieldName, Optional.ofNullable(getter.invoke(object)));
//            } catch (Exception ignored) {
//            }
//        }
//    }
//}
