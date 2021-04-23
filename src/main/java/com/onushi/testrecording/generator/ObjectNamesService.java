package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import org.springframework.stereotype.Service;

// TODO IB There is another test recorder http://testrecorder.amygdalum.net/index.html
@Service
public class ObjectNamesService {
    private final ClassInfoService classInfoService;

    public ObjectNamesService(ClassInfoService classInfoService) {
        this.classInfoService = classInfoService;
    }

    public String generateObjectName(TestGenInfo testGenInfo, Object object) {
        if (testGenInfo.getObjectNames().containsKey(object)) {
            return testGenInfo.getObjectNames().get(object);
        } else {
            String newObjectName = getNewObjectName(testGenInfo, object);
            testGenInfo.getObjectNames().put(object, newObjectName);
            return newObjectName;
        }
    }

    private String getNewObjectName(TestGenInfo testGenInfo, Object object) {
        String objectNameBase = classInfoService.getObjectNameBase(object);
        int newIndex;
        if (testGenInfo.getLastIndexForObjectName().containsKey(objectNameBase)) {
            newIndex = testGenInfo.getLastIndexForObjectName().get(objectNameBase) + 1;
        } else {
            newIndex = 1;
        }
        testGenInfo.getLastIndexForObjectName().put(objectNameBase, newIndex);
        return objectNameBase + newIndex;
    }
}
