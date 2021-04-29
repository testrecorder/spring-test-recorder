package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.object.ObjectCodeGenerator;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGeneratorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

// TODO IB !!!! write unit test for this
@Service
public class TestObjectsManagerService {
    private ObjectCodeGeneratorFactory objectCodeGeneratorFactory;
    private ObjectNameGenerator objectNameGenerator;

    @Autowired
    public void setObjectCodeGeneratorFactory(ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    @Autowired
    public void setObjectNameGenerator(ObjectNameGenerator objectNameGenerator) {
        this.objectNameGenerator = objectNameGenerator;
    }

    public ObjectCodeGenerator getNamedObjectCodeGenerator(TestGenerator testGenerator, Object object, String preferredName) {
        return objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, object, preferredName);
    }

    public ObjectCodeGenerator getCommonObjectCodeGenerator(TestGenerator testGenerator, Object object) {
        Map<Object, ObjectCodeGenerator> objectCache = testGenerator.getObjectObjectCodeGeneratorCache();
        ObjectCodeGenerator existingObject = objectCache.get(object);
        if (existingObject != null) {
            return existingObject;
        } else {
            String objectName = objectNameGenerator.getNewObjectName(testGenerator, object);
            ObjectCodeGenerator objectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(testGenerator, object, objectName);
            objectCache.put(object, objectCodeGenerator);
            return objectCodeGenerator;
        }
    }
}
