package com.onushi.testrecording.analyzer.object;

import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObjectStateReaderService {
    private final ObjectNameGenerator objectNameGenerator;

    public ObjectStateReaderService(ObjectNameGenerator objectNameGenerator) {
        this.objectNameGenerator = objectNameGenerator;
    }

    public Map<String, Object> getObjectState(Object object) {
        Map<String, Object> result = new HashMap<>();
        if (object != null) {
            getFromPublicGetters(object, result);
            getFromPublicFields(object, result);
        }
        return result;
    }

    private void getFromPublicGetters(Object object, Map<String, Object> result) {
        List<Method> getters = Arrays.stream(object.getClass().getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getName().startsWith("get"))
                .collect(Collectors.toList());
        for (Method getter: getters) {
            String fieldName = getter.getName().substring(3);
            fieldName = objectNameGenerator.lowerCaseFirstLetter(fieldName);
            try {
                result.put(fieldName, getter.invoke(object));
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        }
    }

    private void getFromPublicFields(Object object, Map<String, Object> result) {
        List<Field> fields = Arrays.stream(object.getClass().getFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .collect(Collectors.toList());
        for (Field field: fields) {
            try {
                result.put(field.getName(), field.get(object));
            } catch (IllegalAccessException ignored) {
            }
        }
    }
}
