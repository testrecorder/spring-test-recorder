package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.object.ObjectCodeGenerator;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGeneratorFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

// TODO IB !!!! write unit test for this
@Service
public class TestObjectsManagerService {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;
    private final ObjectNameGenerator objectNameGenerator;

    public TestObjectsManagerService(ObjectCodeGeneratorFactory objectCodeGeneratorFactory, ObjectNameGenerator objectNameGenerator) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
        this.objectNameGenerator = objectNameGenerator;
    }

    public ObjectCodeGenerator getNamedObjectCodeGenerator(TestGenerator testGenerator, Object object, String preferredName) {
        // TODO IB !!!! send testGenerator as param
        return objectCodeGeneratorFactory.createObjectCodeGenerator(object, preferredName);
    }

    public ObjectCodeGenerator getCommonObjectCodeGenerator(TestGenerator testGenerator, Object object) {
        // TODO IB !!!! send testGenerator as param
        Map<Object, ObjectCodeGenerator> objectCache = testGenerator.getObjectObjectCodeGeneratorCache();
        ObjectCodeGenerator existingObject = objectCache.get(object);
        if (existingObject != null) {
            return existingObject;
        } else {
            String objectName = objectNameGenerator.getNewObjectName(testGenerator, object);
            ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(object, objectName);
            objectCache.put(object, objectCodeGenerator);
            return objectCodeGenerator;
        }
    }
}
