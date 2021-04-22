package com.onushi.testrecording.analizer.utils;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ClassService {

    public String getFullClassName(Object object) {
        if (object == null) {
            return "null";
        } else {
            return object.getClass().getName();
        }
    }

    public String getShortClassName(Object object) {
        String fullClassName = getFullClassName(object);
        int lastPointIndex = fullClassName.lastIndexOf(".");
        if (lastPointIndex != -1) {
            return fullClassName.substring(lastPointIndex + 1);
        } else {
            return fullClassName;
        }
    }

    public String getPackageName(Object object) {
        String fullClassName = getFullClassName(object);
        int lastPointIndex = fullClassName.lastIndexOf(".");
        if (lastPointIndex != -1) {
            return fullClassName.substring(0, lastPointIndex);
        } else {
            return "";
        }
    }

    public String getObjectNameBase(Object object) {
        String getShortClassName = getShortClassName(object);
        return getShortClassName.substring(0,1).toLowerCase(Locale.ROOT) + getShortClassName.substring(1);
    }
}
