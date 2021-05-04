package com.onushi.testrecording.analyzer.object;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldValue {
    private Object value;
    private Class<?> clazz;
    // TODO IB !!!! rename this to avoid confusions
    private FieldValueType fieldValueType;
}
