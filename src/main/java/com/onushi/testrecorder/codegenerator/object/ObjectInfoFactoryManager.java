package com.onushi.testrecorder.codegenerator.object;

import com.onushi.testrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecorder.analyzer.object.FieldValueStatus;
import com.onushi.testrecorder.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecorder.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecorder.codegenerator.template.StringService;
import com.onushi.testrecorder.codegenerator.test.ObjectNameGenerator;
import com.onushi.testrecorder.codegenerator.test.TestGenerator;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ObjectInfoFactoryManager {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;
    private final ObjectNameGenerator objectNameGenerator;
    private final List<ObjectInfoFactory> knownClassesFactoriesList;
    private final List<ObjectInfoFactory> unknownClassesFactoriesList;
    private final CglibService cglibService;

    public ObjectInfoFactoryManager(ClassInfoService classInfoService,
                                    ObjectStateReaderService objectStateReaderService,
                                    ObjectNameGenerator objectNameGenerator,
                                    ObjectCreationAnalyzerService objectCreationAnalyzerService,
                                    CglibService cglibService,
                                    StringService stringService) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
        this.objectNameGenerator = objectNameGenerator;
        this.cglibService = cglibService;

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
                new ObjectInfoFactoryWithNoArgsConstructorImpl(this,
                        classInfoService, objectCreationAnalyzerService),
                new ObjectInfoFactoryWithLombokBuilderImpl(this,
                        classInfoService, objectCreationAnalyzerService),
                new ObjectInfoFactoryWithAllArgsConstructorImpl(this,
                        objectCreationAnalyzerService, classInfoService),
                new ObjectInfoFactoryWithNoArgsAndSettersImpl(this,
                        classInfoService, objectCreationAnalyzerService),
                new ObjectInfoFactoryWithNoArgsAndFieldsImpl(this,
                        objectCreationAnalyzerService, classInfoService)
        );
    }

    // Cannot be moved to a separate cache class since it will result in cyclic dependency
    // TODO IB if target is already in cache ... I should use it
    public ObjectInfo getNamedObjectInfo(TestGenerator testGenerator, Object object, String preferredName) {
        return createObjectInfo(testGenerator, object, preferredName);
    }

    public ObjectInfo getCommonObjectInfo(TestGenerator testGenerator, Object object) {
        ObjectInfo existingObject = testGenerator.getObjectInfoCache().get(object);
        if (existingObject != null) {
            return existingObject;
        } else {
            if (testGenerator.getObjectsPendingInit().contains(object)) {
                ObjectInfoCreationContext context = new ObjectInfoCreationContext();
                context.setTestGenerator(testGenerator);
                context.setObject(object);
                context.setObjectName("dummy");
                return new ObjectInfoFactoryForCyclicDependencyImpl().createObjectInfo(context);
            }
            testGenerator.getObjectsPendingInit().add(object);
            String objectName = objectNameGenerator.getNewObjectName(testGenerator, object);
            ObjectInfo objectInfo = createObjectInfo(testGenerator, object, objectName);
            testGenerator.getObjectInfoCache().put(object, objectInfo);
            testGenerator.getObjectsPendingInit().remove(object);
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
            removeProxy(context);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ObjectInfoFactoryForNotRedFields().createObjectInfo(context);
        }

        // after we tried knownClassesFactoriesList and we removed the proxy
        // getObjectState to try creating in a generic way
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

        return new ObjectInfoFactoryFallbackImpl(this, classInfoService)
                .createObjectInfo(context);
    }

    private void removeProxy(ObjectInfoCreationContext context) throws Exception {
        context.setObject(cglibService.getProxyTargetObject(context.getObject()));
        int $$index = context.getObjectName().indexOf("$$");
        if ($$index != -1) {
            context.setObjectName(context.getObjectName().substring(0, $$index));
        }
    }
}
