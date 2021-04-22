package com.onushi.testrecording.analizer.test;

import com.onushi.testrecording.analizer.utils.ClassHelper;

import java.util.HashMap;
import java.util.Map;

public class ObjectNameGeneratorImpl implements ObjectNameGenerator {
    private final Map<Object, String> objectNames = new HashMap<>();
    private final Map<String, Integer> lastIndexForClass = new HashMap<>();

    @Override
    public String generateObjectName(Object object) {
        if (objectNames.containsKey(object)) {
            return objectNames.get(object);
        } else {
            String newObjectName = getNewObjectName(object);
            objectNames.put(object, newObjectName);
            return newObjectName;
        }
    }

    private String getNewObjectName(Object object) {
        String objectNameBase = ClassHelper.getObjectNameBase(object);
        int newIndex;
        if (lastIndexForClass.containsKey(objectNameBase)) {
            newIndex = lastIndexForClass.get(objectNameBase) + 1;
        } else {
            newIndex = 1;
        }
        lastIndexForClass.put(objectNameBase, newIndex);
        return objectNameBase + newIndex;
    }
}
