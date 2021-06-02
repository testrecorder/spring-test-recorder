package com.onushi.testrecording.analyzer.object;

import com.onushi.testrecording.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ObjectReadAnalyzerService {
    // TODO IB !!!! move to ClassInfo?
    public List<Method> getPublicGetters(Object object) {
        return Arrays.stream(object.getClass().getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getParameterTypes().length == 0)
                .filter(method -> (method.getName().startsWith("get") ||
                        // TODO IB !!!! should be followed by big letter
                        method.getName().startsWith("is")) &&
                        !method.getName().equals("getClass"))
                .sorted(Comparator.comparing(Method::getName))
                .collect(Collectors.toList());
    }

    public List<Field> getPublicFields(Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .filter(field -> !Modifier.isTransient(field.getModifiers()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

// TODO IB !!!! do we need something from here?
//    public Map<String, FieldReadInfo> getObjectReadInfo(Object object) throws InvocationTargetException, IllegalAccessException {
//        Map<String, FieldReadInfo> results = new HashMap<>();
//
//        List<Method> possibleGetters = Arrays.stream(object.getClass().getMethods())
//                .filter(method -> Modifier.isPublic(method.getModifiers()))
//                .filter(method -> !Modifier.isStatic(method.getModifiers()))
//                .filter(method -> method.getParameterTypes().length == 0)
//                .filter(method -> (method.getName().startsWith("get") || method.getName().startsWith("is")) &&
//                        !method.getName().equals("getClass"))
//                .collect(Collectors.toList());
//
//        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(object);
//        for (Map.Entry<String, FieldValue> entry : objectState.entrySet()) {
//            FieldValue fieldValue = entry.getValue();
//            Field field = fieldValue.getField();
//            if ((!Modifier.isTransient(field.getModifiers())) &&
//                (!Modifier.isStatic(field.getModifiers())) &&
//                (Modifier.isPublic(field.getModifiers()))) {
//                FieldReadInfo fieldReadInfo = FieldReadInfo.builder()
//                        .fieldReadType(FieldReadType.PUBLIC_FIELD)
//                        .fieldValue(fieldValue)
//                        .getter(null)
//                        .value(fieldValue.getValue())
//                        .build();
//                results.put(entry.getKey(), fieldReadInfo);
//                continue;
//            }
//
//            String getterName = getGetterName(field);
//            Method getter = possibleGetters.stream().filter(x -> x.getName().equals(getterName))
//                    .findFirst().orElse(null);
//            if (getter != null) {
//                // Otherwise there might be some confusions about the the field/getter names
//                FieldReadInfo fieldReadInfo = FieldReadInfo.builder()
//                        .fieldReadType(FieldReadType.PUBLIC_GETTER)
//                        .fieldValue(fieldValue)
//                        .getter(getter)
//                        .value(getter.invoke(object))
//                        .build();
//                results.put(entry.getKey(), fieldReadInfo);
//                continue;
//            }
//
//            FieldReadInfo fieldReadInfo = FieldReadInfo.builder()
//                    .fieldReadType(FieldReadType.COULD_NOT_READ)
//                    .fieldValue(fieldValue)
//                    .getter(null)
//                    .value(null)
//                    .build();
//            results.put(entry.getKey(), fieldReadInfo);
//        }
//
//        return results;
//    }

//    private String getGetterName(Field field) {
//        String fieldName = field.getName();
//        if ((field.getType() == boolean.class || field.getType() == Boolean.class) &&
//                (fieldName.matches("^is[A-Z].*$"))) {
//            return "is" + fieldName.substring(2);
//        }
//        return "get" + stringService.upperCaseFirstLetter(fieldName);
//    }
}
