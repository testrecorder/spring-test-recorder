package com.onushi.testrecording.analizer;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

// TODO IB !!!! You cannot inject a component in a non-component
// TODO IB !!!! Make all testable by mocking
//@Component
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObjectNameGenerator {
    private Map<Object, String> objectNames = new HashMap<>();
    private Map<String, Integer> lastIndexForClass = new HashMap<>();

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
        String className = getClassName(object);
        String objectNameRoot = className.substring(0,1).toLowerCase(Locale.ROOT) + className.substring(1);
        Integer newIndex;
        if (lastIndexForClass.containsKey(objectNameRoot)) {
            newIndex = lastIndexForClass.get(objectNameRoot) + 1;
        } else {
            newIndex = 1;
        }
        lastIndexForClass.put(objectNameRoot, newIndex);
        return objectNameRoot + newIndex;
    }

    private String getClassName(Object object) {
        if (object == null) {
            return "null";
        } else {
            String fullClassName = object.getClass().getName();
            int lastPointIndex = fullClassName.lastIndexOf(".");
            if (lastPointIndex != -1) {
                return fullClassName.substring(lastPointIndex + 1);
            } else {
                return fullClassName;
            }
        }
    }
}
