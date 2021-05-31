package com.onushi.testrecording.analyzer.object;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


@Service
public class ObjectReadAnalyzerService {
    private final ObjectStateReaderService objectStateReaderService;

    public ObjectReadAnalyzerService(ObjectStateReaderService objectStateReaderService) {
        this.objectStateReaderService = objectStateReaderService;
    }

    // TODO IB !!!! test
    public Map<String, FieldReadInfo> getObjectReadInfo(Object object) {
        Map<String, FieldReadInfo> results = new HashMap<>();

        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(object);
        for (Map.Entry<String, FieldValue> entry : objectState.entrySet()) {
            FieldValue fieldValue = entry.getValue();
            Field field = fieldValue.getField();
            if ((!Modifier.isTransient(field.getModifiers())) &&
                (!Modifier.isStatic(field.getModifiers())) &&
                (Modifier.isPublic(field.getModifiers()))) {
                FieldReadInfo fieldReadInfo = FieldReadInfo.builder()
                        .fieldReadType(FieldReadType.PUBLIC_FIELD)
                        .fieldValue(fieldValue)
                        .getter(null)
                        .value(fieldValue.getValue())
                        .build();
                results.put(entry.getKey(), fieldReadInfo);
            }

            // TODO IB !!!! implement also getters here
        }

        return results;
    }
}
