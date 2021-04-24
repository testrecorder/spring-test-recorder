package com.onushi.testrecording.analizer.object;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ObjectStateReaderService {
    public Map<String, Object> readObjectState(Object object) {
        Map<String, Object> result = new HashMap<>();
        if (object != null) {
            List<Field> fields = Arrays.stream(object.getClass().getFields())
                    .filter(x -> Modifier.isPublic(x.getModifiers()))
                    .collect(Collectors.toList());
            for (Field field: fields) {
                try {
                    result.put(field.getName(), field.get(object));
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return result;
    }
}
