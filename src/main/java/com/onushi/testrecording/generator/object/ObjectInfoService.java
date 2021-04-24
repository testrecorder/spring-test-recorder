package com.onushi.testrecording.generator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import org.springframework.stereotype.Component;

// TODO IB handle also array and void
// TODO IB LATER generics
// TODO IB some functions alter the arguments or the target object
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
                return new ObjectInfo(object, objectName, true, "null");
            case "java.lang.Float":
                return new ObjectInfo(object, objectName, true, object + "f");
            case "java.lang.Long":
                return new ObjectInfo(object, objectName, true, object + "L");
            case "java.lang.Byte":
                return new ObjectInfo(object, objectName, true, "(byte)" + object);
            case "java.lang.Short":
                return new ObjectInfo(object, objectName, true, "(short)" + object);
            case "java.lang.Character":
                return new ObjectInfo(object, objectName, true, "'" + object + "'");
            case "java.lang.String":
                return new ObjectInfo(object, objectName, true, "\"" + object + "\"");
            case "java.lang.Boolean":
            case "java.lang.Integer":
            case "java.lang.Double":
                return new ObjectInfo(object, objectName, true, object.toString());
            case "java.util.Date":
                return new DateObjectInfo(object, objectName);
            default:
                return new ObjectInfo(object, objectName, true, object.toString());
        }
    }
}
