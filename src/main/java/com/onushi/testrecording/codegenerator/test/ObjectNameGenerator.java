package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import org.springframework.stereotype.Service;

import java.util.Locale;

// TODO IB There is another test recorder http://testrecorder.amygdalum.net/index.html
@Service
public class ObjectNameGenerator {
    private final ClassInfoService classInfoService;

    public ObjectNameGenerator(ClassInfoService classInfoService) {
        this.classInfoService = classInfoService;
    }

    public String getObjectName(TestGenenerator testGenenerator, Object object) {
        if (testGenenerator.getObjectNames().containsKey(object)) {
            return testGenenerator.getObjectNames().get(object);
        } else {
            String newObjectName = getNewObjectName(testGenenerator, object);
            testGenenerator.getObjectNames().put(object, newObjectName);
            return newObjectName;
        }
    }

    public String getBaseObjectName(Object object) {
        String getShortClassName = classInfoService.getShortClassName(object);
        // TODO IB !!!! extract function
        return getShortClassName.substring(0,1).toLowerCase(Locale.ROOT) + getShortClassName.substring(1);
    }


    private String getNewObjectName(TestGenenerator testGenenerator, Object object) {
        String objectNameBase = getBaseObjectName(object);
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
