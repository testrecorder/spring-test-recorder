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
public class ObjectCodeGeneratorFactoryManager {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;
    private final ObjectNameGenerator objectNameGenerator;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectCodeGeneratorFactoryManager(ClassInfoService classInfoService,
                                             ObjectStateReaderService objectStateReaderService,
                                             ObjectNameGenerator objectNameGenerator,
                                             ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
        this.objectNameGenerator = objectNameGenerator;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    public ObjectCodeGenerator getNamedObjectCodeGenerator(TestGenerator testGenerator, Object object, String preferredName) {
        return createObjectCodeGenerator(testGenerator, object, preferredName);
    }

    // TODO IB !!!! move to a separate cache class
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

    // TODO IB !!!! Have a ObjectCreationContext that knows more and more about the object as it try to create it.
    // TODO IB !!!! Have a List of factories that are tried one by one. They receive the ObjectCreationContext
    // TODO IB !!!! all factories implement an interface
    // TODO IB !!!! all factories are created with new and receive the dependencies from this
    protected ObjectCodeGenerator createObjectCodeGenerator(TestGenerator testGenerator, Object object, String objectName) {
        ObjectCodeGeneratorCreationContext context = new ObjectCodeGeneratorCreationContext();
        context.setTestGenerator(testGenerator);
        context.setObject(object);
        context.setObjectName(objectName);

        // TODO IB !!!! objectState in context lazy
        // TODO IB !!!! move this list upper
        // TODO IB !!!! reorder params here
        List<ObjectCodeGeneratorFactory> factories = Arrays.asList(
                new ObjectCodeGeneratorFactoryForNullImpl(),
                new ObjectCodeGeneratorFactoryForPrimitiveImpl(),
                new ObjectCodeGeneratorFactoryForDateImpl(),
                new ObjectCodeGeneratorFactoryForArrayImpl(this),
                new ObjectCodeGeneratorFactoryForArrayListImpl(this),
                new ObjectCodeGeneratorFactoryWithNoArgsConstructorImpl(objectCreationAnalyzerService),
                new ObjectCodeGeneratorFactoryWithLombokBuilderImpl(classInfoService,
                        objectStateReaderService, this, objectCreationAnalyzerService),
                new ObjectCodeGeneratorFactoryWithAllArgsConstructorImpl(this, objectCreationAnalyzerService),
                new ObjectCodeGeneratorFactoryWithNoArgsAndFieldsImpl(this, objectStateReaderService, objectCreationAnalyzerService)
        );
        for (ObjectCodeGeneratorFactory factory : factories) {
            ObjectCodeGenerator objectCodeGenerator = factory.createObjectCodeGenerator(context);
            if (objectCodeGenerator != null) {
                return objectCodeGenerator;
            }
        }

        return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), object.toString(), "Object");
    }
}
