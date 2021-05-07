package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ObjectCodeGeneratorFactoryManager implements ObjectCodeGeneratorFactory {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;
    private final ObjectNameGenerator objectNameGenerator;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;
    private final List<ObjectCodeGeneratorFactory> factoriesList;

    public ObjectCodeGeneratorFactoryManager(ClassInfoService classInfoService,
                                             ObjectStateReaderService objectStateReaderService,
                                             ObjectNameGenerator objectNameGenerator,
                                             ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
        this.objectNameGenerator = objectNameGenerator;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;

        factoriesList = Arrays.asList(
                new ObjectCodeGeneratorFactoryForNullImpl(),
                new ObjectCodeGeneratorFactoryForPrimitiveImpl(),
                new ObjectCodeGeneratorFactoryForDateImpl(),
                new ObjectCodeGeneratorFactoryForArrayImpl(this),
                new ObjectCodeGeneratorFactoryForArrayListImpl(this),
                new ObjectCodeGeneratorFactoryWithNoArgsConstructorImpl(objectCreationAnalyzerService),
                new ObjectCodeGeneratorFactoryWithLombokBuilderImpl(this, classInfoService,
                        objectStateReaderService, objectCreationAnalyzerService),
                new ObjectCodeGeneratorFactoryWithAllArgsConstructorImpl(this, objectCreationAnalyzerService),
                new ObjectCodeGeneratorFactoryWithNoArgsAndFieldsImpl(this, objectStateReaderService, objectCreationAnalyzerService)
        );
    }

    // Cannot be moved to a separate cache class since it will result in cyclic dependency
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
        ObjectCodeGeneratorCreationContext context = new ObjectCodeGeneratorCreationContext();
        context.setTestGenerator(testGenerator);
        context.setObject(object);
        context.setObjectName(objectName);
        return createObjectCodeGenerator(context);
    }

    // TODO IB !!!! Put more in ObjectCreationContext. objectState if needed
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        for (ObjectCodeGeneratorFactory factory : factoriesList) {
            ObjectCodeGenerator objectCodeGenerator = factory.createObjectCodeGenerator(context);
            if (objectCodeGenerator != null) {
                return objectCodeGenerator;
            }
        }

        return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject().toString(), "Object");
    }
}
