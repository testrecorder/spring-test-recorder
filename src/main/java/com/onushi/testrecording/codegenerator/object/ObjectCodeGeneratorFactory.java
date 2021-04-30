package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ObjectCodeGeneratorFactory {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;
    private final SimpleObjectCodeGeneratorFactory simpleObjectCodeGeneratorFactory;
    private final ObjectNameGenerator objectNameGenerator;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectCodeGeneratorFactory(ClassInfoService classInfoService,
                                      ObjectStateReaderService objectStateReaderService,
                                      ObjectNameGenerator objectNameGenerator,
                                      ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
        this.objectNameGenerator = objectNameGenerator;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
        // TODO IB is it ok to have new here?
        this.simpleObjectCodeGeneratorFactory = new SimpleObjectCodeGeneratorFactory();
    }

    public ObjectCodeGenerator getNamedObjectCodeGenerator(TestGenerator testGenerator, Object object, String preferredName) {
        return createObjectCodeGenerator(testGenerator, object, preferredName);
    }

    public ObjectCodeGenerator getCommonObjectCodeGenerator(TestGenerator testGenerator, Object object) {
        Map<Object, ObjectCodeGenerator> objectCache = testGenerator.getObjectCodeGeneratorCache();
        ObjectCodeGenerator existingObject = objectCache.get(object);
        if (existingObject != null) {
            return existingObject;
        } else {
            String objectName = objectNameGenerator.getNewObjectName(testGenerator, object);
            ObjectCodeGenerator objectCodeGenerator = createObjectCodeGenerator(testGenerator, object, objectName);
            objectCache.put(object, objectCodeGenerator);
            return objectCodeGenerator;
        }
    }

    protected ObjectCodeGenerator createObjectCodeGenerator(TestGenerator testGenerator, Object object, String objectName) {
        if (object == null) {
            return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(null, objectName, "null", "null");
        }

        String fullClassName = object.getClass().getName();
        switch (fullClassName) {
            case "java.lang.Float":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object + "f", "float");
            case "java.lang.Long":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object + "L", "long");
            case "java.lang.Byte":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, "(byte)" + object, "byte");
            case "java.lang.Short":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, "(short)" + object, "short");
            case "java.lang.Character":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, "'" + object + "'", "char");
            case "java.lang.String":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, "\"" + object + "\"", "String");
            case "java.lang.Boolean":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object.toString(), "boolean");
            case "java.lang.Integer":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object.toString(), "int");
            case "java.lang.Double":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object.toString(), "double");
            case "java.util.Date":
                return new DateObjectCodeGeneratorFactory().createObjectCodeGenerator(object, objectName);
            default:
                if (fullClassName.startsWith("[")) {
                    return new ArrayObjectCodeGeneratorFactory(this).createObjectCodeGenerator(testGenerator, object, objectName);
                } else if (object instanceof List<?> ) {
                    return new ArrayListCodeGeneratorFactory(this).createObjectCodeGenerator(testGenerator, object, objectName);
                // } else if (classInfoService.hasPublicNoArgsConstructor(object.getClass()) )
                } else if (objectCreationAnalyzerService.canBeCreatedWithLombokBuilder(object)) {
                    ObjectCodeGeneratorWithLombokBuilderFactory objectCodeGeneratorWithLombokBuilderFactory =
                            new ObjectCodeGeneratorWithLombokBuilderFactory(classInfoService,
                            objectStateReaderService,
                            this);

                    return objectCodeGeneratorWithLombokBuilderFactory.createObjectCodeGenerator(testGenerator, object, objectName);
                } else {
                    return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName, object.toString(), "Object");
                }
        }
    }
}
