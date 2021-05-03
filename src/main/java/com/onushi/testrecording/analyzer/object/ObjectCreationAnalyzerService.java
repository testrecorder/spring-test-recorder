package com.onushi.testrecording.analyzer.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ObjectCreationAnalyzerService {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;

    public ObjectCreationAnalyzerService(ClassInfoService classInfoService, ObjectStateReaderService objectStateReaderService) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
    }

    public boolean canBeCreatedWithLombokBuilder(Object object) {
        if (object == null) {
            return false;
        } else {
            return classInfoService.canBeCreatedWithLombokBuilder(object.getClass());
        }
    }

    public boolean canBeCreatedWithNoArgsConstructor(Object object) {
        if (object == null) {
            return false;
        } else {
            Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(object);

            long valuesDifferentThanDefaults = 0;
            for (FieldValue fieldValue : objectState.values()) {
                if (fieldValue.getFieldValueType() == FieldValueType.COULD_NOT_READ) {
                    continue;
                }
                if (fieldValue.getFieldValueType() == FieldValueType.VALUE_READ) {
                    if (isDefaultValueForItsClass(fieldValue.getValue())) {
                        continue;
                    }
                }

                valuesDifferentThanDefaults++;
            }
            return classInfoService.hasPublicNoArgsConstructor(object.getClass()) &&
                    valuesDifferentThanDefaults == 0;
        }
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

    // TODO IB !!!! add more here
}
