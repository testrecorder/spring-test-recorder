package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassNameService;
import org.springframework.stereotype.Component;

// TODO IB handle also array and void
// TODO IB LATER generics
// TODO IB some functions alter the arguments or the target object
@Component
public class ObjectCodeGeneratorFactory {
    private final ClassNameService classNameService;

    public ObjectCodeGeneratorFactory(ClassNameService classNameService) {
        this.classNameService = classNameService;
    }

    public ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        String fullClassName = classNameService.getFullClassName(object);
        switch (fullClassName) {
            case "null":
                return new SimpleObjectCodeGenerator(object, objectName, "null");
            case "java.lang.Float":
                return new SimpleObjectCodeGenerator(object, objectName, object + "f");
            case "java.lang.Long":
                return new SimpleObjectCodeGenerator(object, objectName, object + "L");
            case "java.lang.Byte":
                return new SimpleObjectCodeGenerator(object, objectName, "(byte)" + object);
            case "java.lang.Short":
                return new SimpleObjectCodeGenerator(object, objectName, "(short)" + object);
            case "java.lang.Character":
                return new SimpleObjectCodeGenerator(object, objectName, "'" + object + "'");
            case "java.lang.String":
                return new SimpleObjectCodeGenerator(object, objectName, "\"" + object + "\"");
            case "java.lang.Boolean":
            case "java.lang.Integer":
            case "java.lang.Double":
                return new SimpleObjectCodeGenerator(object, objectName, object.toString());
            case "java.util.Date":
                return new DateObjectCodeGenerator(object, objectName);
            default:
                // TODO IB !!!! test that builder is present
                return new ObjectCodeGeneratorWithBuilder(object, objectName);
        }
    }
}
