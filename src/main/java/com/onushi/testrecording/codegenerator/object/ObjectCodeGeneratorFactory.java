package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.object.ObjectStateReaderService;
import org.springframework.stereotype.Component;

@Component
public class ObjectCodeGeneratorFactory {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;
    private final SimpleObjectCodeGeneratorFactory simpleObjectCodeGeneratorFactory;

    public ObjectCodeGeneratorFactory(ClassInfoService classInfoService, ObjectStateReaderService objectStateReaderService) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
        // TODO IB is it ok to have new here?
        this.simpleObjectCodeGeneratorFactory = new SimpleObjectCodeGeneratorFactory();
    }

    public ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        if (object == null) {
            return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(null, objectName, "null");
        }

        String fullClassName = object.getClass().getName();
        switch (fullClassName) {
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
                return new DateObjectCodeGeneratorFactory().createObjectCodeGenerator(object, objectName);
            default:
                if (fullClassName.startsWith("[")) {
                    ArrayObjectCodeGeneratorFactory arrayObjectCodeGeneratorFactory = new ArrayObjectCodeGeneratorFactory(this);
                    return arrayObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName);
                }
                else if (classInfoService.canBeCreatedWithLombokBuilder(object.getClass())) {
                    ObjectCodeGeneratorWithLombokBuilderFactory objectCodeGeneratorWithLombokBuilderFactory =
                            new ObjectCodeGeneratorWithLombokBuilderFactory(classInfoService,
                            this.objectStateReaderService,
                            this);

                    return objectCodeGeneratorWithLombokBuilderFactory.createObjectCodeGenerator(object, objectName);
                } else {
                    return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object.toString());
                }
        }
    }
}
