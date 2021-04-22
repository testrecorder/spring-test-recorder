package com.onushi.testrecording.analizer.test;

import com.onushi.testrecording.analizer.utils.ClassHelper;
import org.springframework.stereotype.Service;

@Service
public class ObjectNameServiceImpl implements ObjectNameService {

    @Override
    public String generateObjectName(TestInfo testInfo, Object object) {
        if (testInfo.getObjectNames().containsKey(object)) {
            return testInfo.getObjectNames().get(object);
        } else {
            String newObjectName = getNewObjectName(testInfo, object);
            testInfo.getObjectNames().put(object, newObjectName);
            return newObjectName;
        }
    }

    private String getNewObjectName(TestInfo testInfo, Object object) {
        String objectNameBase = ClassHelper.getObjectNameBase(object);
        int newIndex;
        if (testInfo.getLastIndexForObjectName().containsKey(objectNameBase)) {
            newIndex = testInfo.getLastIndexForObjectName().get(objectNameBase) + 1;
        } else {
            newIndex = 1;
        }
        testInfo.getLastIndexForObjectName().put(objectNameBase, newIndex);
        return objectNameBase + newIndex;
    }
}
