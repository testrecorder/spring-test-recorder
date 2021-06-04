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
public class ObjectInfoFactoryManager {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;
    private final ObjectNameGenerator objectNameGenerator;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;
    private final List<ObjectInfoFactory> knownClassesFactoriesList;
    private final List<ObjectInfoFactory> unknownClassesFactoriesList;
    private final CglibService cglibService;
    private final StringService stringService;

    public ObjectInfoFactoryManager(ClassInfoService classInfoService,
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
                new ObjectInfoFactoryForNullImpl(),
                new ObjectInfoFactoryForPrimitiveImpl(stringService),
                new ObjectInfoFactoryForEnumImpl(),
                new ObjectInfoFactoryForUUIDImpl(),
                new ObjectInfoFactoryForDateImpl(),
                new ObjectInfoFactoryForArrayImpl(this),
                new ObjectInfoFactoryForArrayListImpl(this),
                new ObjectInfoFactoryForHashMapImpl(this),
                new ObjectInfoFactoryForHashSetImpl(this)
        );
        unknownClassesFactoriesList = Arrays.asList(
                new ObjectInfoFactoryForMockedDependencyImpl(this),
                new ObjectInfoFactoryWithNoArgsConstructorImpl(objectCreationAnalyzerService),
                new ObjectInfoFactoryWithLombokBuilderImpl(this,
                        classInfoService, objectCreationAnalyzerService),
                new ObjectInfoFactoryWithAllArgsConstructorImpl(this,
                        objectCreationAnalyzerService),
                new ObjectInfoFactoryWithNoArgsAndSettersImpl(this,
                        classInfoService, objectCreationAnalyzerService),
                new ObjectInfoFactoryWithNoArgsAndFieldsImpl(this,
                        objectCreationAnalyzerService)
        );
    }

    // Cannot be moved to a separate cache class since it will result in cyclic dependency
    // TODO IB if target is already in cache ... I should use it
    public ObjectInfo getNamedObjectInfo(TestGenerator testGenerator, Object object, String preferredName) {
        return createObjectInfo(testGenerator, object, preferredName);
    }

    public ObjectInfo getCommonObjectInfo(TestGenerator testGenerator, Object object) {
        Map<Object, ObjectInfo> objectCache = testGenerator.getObjectInfoCache();
        ObjectInfo existingObject = objectCache.get(object);
        if (existingObject != null) {
            return existingObject;
        } else {
            String objectName = objectNameGenerator.getNewObjectName(testGenerator, object);
            ObjectInfo objectInfo = createObjectInfo(testGenerator, object, objectName);
            objectCache.put(object, objectInfo);
            return objectInfo;
        }
    }

    protected ObjectInfo createObjectInfo(TestGenerator testGenerator, Object object, String objectName) {
        ObjectInfoCreationContext context = new ObjectInfoCreationContext();
        context.setTestGenerator(testGenerator);
        context.setObject(object);
        context.setObjectName(objectName);

        for (ObjectInfoFactory factory : knownClassesFactoriesList) {
            ObjectInfo objectInfo = factory.createObjectInfo(context);
            if (objectInfo != null) {
                return objectInfo;
            }
        }

        try {
            unproxyObject(context);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ObjectInfoFactoryForNotRedFields().createObjectInfo(context);
        }

        // after we tried knownClassesFactoriesList and we unproxy we getObjectState to try creating in a generic way
        context.setObjectState(objectStateReaderService.getObjectState(context.getObject()));
        boolean allFieldsAreRead = context.getObjectState().values().stream()
                .allMatch(x -> x.getFieldValueStatus() == FieldValueStatus.VALUE_READ);
        if (!allFieldsAreRead) {
            return new ObjectInfoFactoryForNotRedFields().createObjectInfo(context);
        }

        for (ObjectInfoFactory factory : unknownClassesFactoriesList) {
            ObjectInfo objectInfo = factory.createObjectInfo(context);
            if (objectInfo != null) {
                return objectInfo;
            }
        }

        return new ObjectInfoFactoryFallbackImpl().createObjectInfo(context);
    }

    private void unproxyObject(ObjectInfoCreationContext context) throws Exception {
        context.setObject(cglibService.getUnproxiedObject(context.getObject()));
        int $$index = context.getObjectName().indexOf("$$");
        if ($$index != -1) {
            context.setObjectName(context.getObjectName().substring(0, $$index));
        }
    }
}
