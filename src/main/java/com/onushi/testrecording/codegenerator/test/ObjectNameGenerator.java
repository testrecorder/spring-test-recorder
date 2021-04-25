package com.onushi.testrecording.codegenerator.test;

import org.springframework.stereotype.Service;

import java.util.Locale;

// TODO IB There is another test recorder http://testrecorder.amygdalum.net/index.html
@Service
public class ObjectNameGenerator {
    public String getObjectName(TestGenerator testGenerator, Object object) {
        if (testGenerator.getObjectNames().containsKey(object)) {
            return testGenerator.getObjectNames().get(object);
        } else {
            String newObjectName = getNewObjectName(testGenerator, object);
            testGenerator.getObjectNames().put(object, newObjectName);
            return newObjectName;
        }
    }

    public String getBaseObjectName(Object object) {
        if (object == null) {
            return "null";
        } else {
            return lowerCaseFirstLetter(object.getClass().getSimpleName());
        }
    }

    public String lowerCaseFirstLetter(String varName) {
        if (varName == null || varName.length() == 0) {
            throw new IllegalArgumentException("varName");
        }
        return varName.substring(0,1).toLowerCase(Locale.ROOT) + varName.substring(1);
    }

    private String getNewObjectName(TestGenerator testGenerator, Object object) {
        String objectNameBase = getBaseObjectName(object);
        int newIndex;
        if (testGenerator.getLastIndexForObjectName().containsKey(objectNameBase)) {
            newIndex = testGenerator.getLastIndexForObjectName().get(objectNameBase) + 1;
        } else {
            newIndex = 1;
        }
        testGenerator.getLastIndexForObjectName().put(objectNameBase, newIndex);
        return objectNameBase + newIndex;
    }
}
