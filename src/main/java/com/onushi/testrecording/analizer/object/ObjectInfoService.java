package com.onushi.testrecording.analizer.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import org.springframework.stereotype.Component;

// TODO IB handle also array and void
// TODO IB LATER generics
@Component
public class ObjectInfoService {
    private final ClassInfoService classInfoService;

    public ObjectInfoService(ClassInfoService classInfoService) {
        this.classInfoService = classInfoService;
    }

    public ObjectInfo createObjectInfo(Object object, String objectName) {
        String fullClassName = classInfoService.getFullClassName(object);
        switch (fullClassName) {
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
}
