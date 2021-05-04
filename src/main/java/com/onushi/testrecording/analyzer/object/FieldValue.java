package com.onushi.testrecording.analyzer.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldValue {
    private Object value;
    private Class<?> clazz;
    private FieldValueType fieldValueType;
}
