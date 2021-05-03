package com.onushi.testrecording.analyzer.object;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObjectStateReaderService {
    public Map<String, FieldValue> getObjectState(Object object) {
        if (object == null) {
            return new HashMap<>();
        }
        Map<String, FieldValue> result = new HashMap<>();
        List<Field> fields = Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> !Modifier.isTransient(field.getModifiers()))
                .collect(Collectors.toList());
        for (Field field : fields) {
            try {
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                result.put(field.getName(), new FieldValue(field.get(object), FieldValueType.VALUE_READ));
            } catch (Exception ignored) {
                result.put(field.getName(), new FieldValue(null, FieldValueType.COULD_NOT_READ));
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
