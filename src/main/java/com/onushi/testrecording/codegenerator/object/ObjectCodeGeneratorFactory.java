package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import org.springframework.stereotype.Component;

// TODO IB handle also array and void
// TODO IB LATER generics
// TODO IB some functions alter the arguments or the target object
@Component
public class ObjectCodeGeneratorFactory {
    private final ClassInfoService classInfoService;

    public ObjectCodeGeneratorFactory(ClassInfoService classInfoService) {
        this.classInfoService = classInfoService;
    }

    public ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        String fullClassName = classInfoService.getFullClassName(object);
        switch (fullClassName) {
            case "null":
                return new ObjectCodeGenerator(object, objectName, true, "null");
            case "java.lang.Float":
                return new ObjectCodeGenerator(object, objectName, true, object + "f");
            case "java.lang.Long":
                return new ObjectCodeGenerator(object, objectName, true, object + "L");
            case "java.lang.Byte":
                return new ObjectCodeGenerator(object, objectName, true, "(byte)" + object);
            case "java.lang.Short":
                return new ObjectCodeGenerator(object, objectName, true, "(short)" + object);
            case "java.lang.Character":
                return new ObjectCodeGenerator(object, objectName, true, "'" + object + "'");
            case "java.lang.String":
                return new ObjectCodeGenerator(object, objectName, true, "\"" + object + "\"");
            case "java.lang.Boolean":
            case "java.lang.Integer":
            case "java.lang.Double":
                return new ObjectCodeGenerator(object, objectName, true, object.toString());
            case "java.util.Date":
                return new DateObjectCodeGenerator(object, objectName);
            default:
                // TODO IB !!!! test that builder is present
                return new ObjectCodeGeneratorWithBuilder(object, objectName);
        }
    }
}
