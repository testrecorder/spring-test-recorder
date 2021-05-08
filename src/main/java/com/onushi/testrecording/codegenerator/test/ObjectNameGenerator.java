package com.onushi.testrecording.codegenerator.test;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ObjectNameGenerator {
    public String getBaseObjectName(Object object) {
        if (object == null) {
            return "null";
        } else {
            String classSimpleName = object.getClass().getSimpleName();
            if (classSimpleName.contains("[")) {
                return "array";
            } else {
                return lowerCaseFirstLetter(object.getClass().getSimpleName());
            }
        }
    }

    // TODO IB !!!! move from here to StringService
    public String lowerCaseFirstLetter(String varName) {
        if (varName == null || varName.length() == 0) {
            throw new IllegalArgumentException("varName");
        }
        return varName.substring(0,1).toLowerCase(Locale.ROOT) + varName.substring(1);
    }

    public String getNewObjectName(TestGenerator testGenerator, Object object) {
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
