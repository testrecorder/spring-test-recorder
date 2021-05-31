package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.FieldValueStatus;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.template.StringService;
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
    private final List<ObjectCodeGeneratorFactory> knownClassesFactoriesList;
    private final List<ObjectCodeGeneratorFactory> unknownClassesFactoriesList;
    private final CglibService cglibService;
    private final StringService stringService;

    public ObjectCodeGeneratorFactoryManager(ClassInfoService classInfoService,
                                             ObjectStateReaderService objectStateReaderService,
                                             ObjectNameGenerator objectNameGenerator,
                                             ObjectCreationAnalyzerService objectCreationAnalyzerService,
                                             CglibService cglibService,
                                             StringService stringService) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
        this.objectNameGenerator = objectNameGenerator;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
        this.cglibService = cglibService;
        this.stringService = stringService;

        knownClassesFactoriesList = Arrays.asList(
                new ObjectCodeGeneratorFactoryForNullImpl(),
                new ObjectCodeGeneratorFactoryForPrimitiveImpl(stringService),
                new ObjectCodeGeneratorFactoryForEnumImpl(),
                new ObjectCodeGeneratorFactoryForDateImpl(),
                new ObjectCodeGeneratorFactoryForArrayImpl(this),
                new ObjectCodeGeneratorFactoryForArrayListImpl(this),
                new ObjectCodeGeneratorFactoryForHashMapImpl(this),
                new ObjectCodeGeneratorFactoryForHashSetImpl(this),
                new ObjectCodeGeneratorFactoryForUUIDImpl()
        );
        unknownClassesFactoriesList = Arrays.asList(
                new ObjectCodeGeneratorFactoryForMockedDependencyImpl(this),
                new ObjectCodeGeneratorFactoryWithNoArgsConstructorImpl(objectCreationAnalyzerService),
                new ObjectCodeGeneratorFactoryWithLombokBuilderImpl(this,
                        classInfoService, objectCreationAnalyzerService),
                new ObjectCodeGeneratorFactoryWithAllArgsConstructorImpl(this,
                        objectCreationAnalyzerService),
                new ObjectCodeGeneratorFactoryWithNoArgsAndSettersImpl(this,
                        classInfoService, objectCreationAnalyzerService),
                new ObjectCodeGeneratorFactoryWithNoArgsAndFieldsImpl(this,
                        objectCreationAnalyzerService)
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

        for (ObjectCodeGeneratorFactory factory : knownClassesFactoriesList) {
            ObjectCodeGenerator objectCodeGenerator = factory.createObjectCodeGenerator(context);
            if (objectCodeGenerator != null) {
                return objectCodeGenerator;
            }
        }

        try {
            unproxyObject(context);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ObjectCodeGeneratorFactoryForNotRedFields().createObjectCodeGenerator(context);
        }

        // after we tried knownClassesFactoriesList and we unproxy we getObjectState to try creating in a generic way
        context.setObjectState(objectStateReaderService.getObjectState(context.getObject()));
        boolean allFieldsAreRead = context.getObjectState().values().stream()
                .allMatch(x -> x.getFieldValueStatus() == FieldValueStatus.VALUE_READ);
        if (!allFieldsAreRead) {
            return new ObjectCodeGeneratorFactoryForNotRedFields().createObjectCodeGenerator(context);
        }

        for (ObjectCodeGeneratorFactory factory : unknownClassesFactoriesList) {
            ObjectCodeGenerator objectCodeGenerator = factory.createObjectCodeGenerator(context);
            if (objectCodeGenerator != null) {
                return objectCodeGenerator;
            }
        }

        return new ObjectCodeGeneratorFactoryFallbackImpl().createObjectCodeGenerator(context);
    }

    private void unproxyObject(ObjectCodeGeneratorCreationContext context) throws Exception {
        context.setObject(cglibService.getUnproxiedObject(context.getObject()));
        int $$index = context.getObjectName().indexOf("$$");
        if ($$index != -1) {
            context.setObjectName(context.getObjectName().substring(0, $$index));
        }
    }
}
