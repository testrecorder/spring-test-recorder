package com.onushi.springtestrecorder.analyzer.object;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

@Data
@AllArgsConstructor
public class FieldValue {
    private Object value;
    private Class<?> clazz;
    private FieldValueStatus fieldValueStatus;
    private Field field;
}
