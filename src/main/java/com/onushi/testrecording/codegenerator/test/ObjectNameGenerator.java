package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analizer.classInfo.ClassNameService;
import org.springframework.stereotype.Service;

// TODO IB There is another test recorder http://testrecorder.amygdalum.net/index.html
@Service
public class ObjectNameGenerator {
    private final ClassNameService classNameService;

    public ObjectNameGenerator(ClassNameService classNameService) {
        this.classNameService = classNameService;
    }

    public String generateObjectName(TestGenenerator testGenenerator, Object object) {
        if (testGenenerator.getObjectNames().containsKey(object)) {
            return testGenenerator.getObjectNames().get(object);
        } else {
            String newObjectName = getNewObjectName(testGenenerator, object);
            testGenenerator.getObjectNames().put(object, newObjectName);
            return newObjectName;
        }
    }

    private String getNewObjectName(TestGenenerator testGenenerator, Object object) {
        String objectNameBase = classNameService.getObjectNameBase(object);
        int newIndex;
        if (testGenenerator.getLastIndexForObjectName().containsKey(objectNameBase)) {
            newIndex = testGenenerator.getLastIndexForObjectName().get(objectNameBase) + 1;
        } else {
            newIndex = 1;
        }
        testGenenerator.getLastIndexForObjectName().put(objectNameBase, newIndex);
        return objectNameBase + newIndex;
    }
}
