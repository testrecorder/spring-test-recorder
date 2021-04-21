package com.onushi.testrecording.analizer.object;

import org.springframework.stereotype.Component;

@Component
public class ObjectInfoFactory {
    public ObjectInfo getObjectInfo(Object object, String objectName) {
        String className = getClassName(object);
        switch (className) {
            case "null":
                return new NullObjectInfo(objectName);
            case "java.lang.Float":
                return new FloatObjectInfo(object, objectName);
            case "java.lang.Long":
                return new LongObjectInfo(object, objectName);
            case "java.lang.Byte":
                return new ByteObjectInfo(object, objectName);
            case "java.lang.Short":
                return new ShortObjectInfo(object, objectName);
            case "java.lang.Character":
                return new CharacterObjectInfo(object, objectName);
            case "java.lang.String":
                return new StringObjectInfo(object, objectName);
            case "java.util.Date":
                return new DateObjectInfo(object, objectName);
            case "java.lang.Boolean":
            case "java.lang.Integer":
            case "java.lang.Double":
            default:
                return new GenericObjectInfo(object, objectName);
        }
    }

    private String getClassName(Object object) {
        if (object == null) {
            return "null";
        } else {
            return object.getClass().getName();
        }
    }
}
