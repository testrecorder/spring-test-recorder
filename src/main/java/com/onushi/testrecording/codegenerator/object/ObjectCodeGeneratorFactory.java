package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.classInfo.MatchingConstructor;
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

    // TODO IB !!!! Have a ObjectCreationContext that knows more and more about the object as it try to create it. Use @Getter(lazy=true)
    // TODO IB !!!! Have a List of factories that are tried one by one. They receive the ObjectCreationContext
    // TODO IB !!!! all factories implement an interface
    // TODO IB !!!! all factories are created with new and receive the dependencies from this
    protected ObjectCodeGenerator createObjectCodeGenerator(TestGenerator testGenerator, Object object, String objectName) {
        ObjectCodeGeneratorCreationContext context = new ObjectCodeGeneratorCreationContext();
        context.setTestGenerator(testGenerator);
        context.setObject(object);
        context.setObjectName(objectName);

        if (object == null) {
            return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, "null", "null");
        }

        String fullClassName = object.getClass().getName();
        switch (fullClassName) {
            case "java.lang.Float":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, object + "f", "float");
            case "java.lang.Long":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, object + "L", "long");
            case "java.lang.Byte":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, "(byte)" + object, "byte");
            case "java.lang.Short":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, "(short)" + object, "short");
            case "java.lang.Character":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, "'" + object + "'", "char");
            case "java.lang.String":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, "\"" + object + "\"", "String");
            case "java.lang.Boolean":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, object.toString(), "boolean");
            case "java.lang.Integer":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, object.toString(), "int");
            case "java.lang.Double":
                return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, object.toString(), "double");
            case "java.util.Date":
                return new DateObjectCodeGeneratorFactory().createObjectCodeGenerator(context);
            default:
                if (fullClassName.startsWith("[")) {
                    return new ArrayObjectCodeGeneratorFactory(this).createObjectCodeGenerator(context);
                } else if (object instanceof List<?>) {
                    return new ArrayListCodeGeneratorFactory(this).createObjectCodeGenerator(context);
                } else if (objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(object)) {
                    return new ObjectCodeGeneratorWithNoArgsConstructorFactory().createObjectCodeGenerator(context);
                } else if (objectCreationAnalyzerService.canBeCreatedWithLombokBuilder(object)) {
                    ObjectCodeGeneratorWithLombokBuilderFactory objectCodeGeneratorWithLombokBuilderFactory =
                            new ObjectCodeGeneratorWithLombokBuilderFactory(classInfoService,
                                    objectStateReaderService,
                                    this);

                    return objectCodeGeneratorWithLombokBuilderFactory.createObjectCodeGenerator(context);
                } else {
                    List<MatchingConstructor> matchingAllArgsConstructors = objectCreationAnalyzerService.getMatchingAllArgsConstructors(object);
                    if (matchingAllArgsConstructors.size() > 0) {
                        MatchingConstructor matchingConstructor = matchingAllArgsConstructors.get(0);
                        boolean moreConstructorsAvailable = matchingAllArgsConstructors.size() > 1;
                        return new ObjectCodeGeneratorWithAllArgsConstructorFactory(this)
                                .createObjectCodeGenerator(object, objectName, testGenerator, matchingConstructor, moreConstructorsAvailable);
//                    } else if (objectCreationAnalyzerService.canBeCreatedWithNoArgsAndSetters(object)) {
//
                    } else if (objectCreationAnalyzerService.canBeCreatedWithNoArgsAndFields(object)) {
                        return new ObjectCodeGeneratorWithNoArgsAndFieldsFactory(this, objectStateReaderService)
                                .createObjectCodeGenerator(context);
                    } else {
                        return simpleObjectCodeGeneratorFactory.createObjectCodeGenerator(context, object.toString(), "Object");
                    }
                }
        }
    }
}
