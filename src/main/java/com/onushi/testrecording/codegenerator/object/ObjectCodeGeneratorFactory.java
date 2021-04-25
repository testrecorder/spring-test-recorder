package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.object.ObjectStateReaderService;
import org.springframework.stereotype.Component;

// TODO IB handle also array and void
// TODO IB LATER generics
// TODO IB some functions alter the arguments or the target object
@Component
public class ObjectCodeGeneratorFactory {
    private final ClassInfoService classInfoService;
    private final SimpleObjectCodeGeneratorFactory simpleObjectCodeGeneratorFactory;
    private final DateObjectCodeGeneratorFactory dateObjectCodeGeneratorFactory;
    private final ObjectCodeGeneratorWithLombokBuilderFactory objectCodeGeneratorWithLombokBuilderFactory;

    public ObjectCodeGeneratorFactory(ClassInfoService classInfoService, ObjectStateReaderService objectStateReaderService) {
        this.classInfoService = classInfoService;
        // TODO IB is it ok to have new here?
        this.simpleObjectCodeGeneratorFactory = new SimpleObjectCodeGeneratorFactory();
        this.dateObjectCodeGeneratorFactory = new DateObjectCodeGeneratorFactory();
        this.objectCodeGeneratorWithLombokBuilderFactory = new ObjectCodeGeneratorWithLombokBuilderFactory(classInfoService,
                objectStateReaderService,
                this);
    }

    public ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        String fullClassName = classInfoService.getFullClassName(object);
        switch (fullClassName) {
            case "null":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, "null");
            case "java.lang.Float":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object + "f");
            case "java.lang.Long":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object + "L");
            case "java.lang.Byte":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, "(byte)" + object);
            case "java.lang.Short":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, "(short)" + object);
            case "java.lang.Character":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, "'" + object + "'");
            case "java.lang.String":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, "\"" + object + "\"");
            case "java.lang.Boolean":
            case "java.lang.Integer":
            case "java.lang.Double":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object.toString());
            case "java.util.Date":
                return dateObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName);
            default:
                if (classInfoService.canBeCreatedWithLombokBuilder(object)) {
                    return objectCodeGeneratorWithLombokBuilderFactory.createObjectCodeGenerator(object, objectName);
                } else {
                    return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object.toString());
                }
        }
    }
}
