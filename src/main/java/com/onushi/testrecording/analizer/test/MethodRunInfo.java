package com.onushi.testrecording.analizer.test;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import lombok.Getter;

import java.util.*;

// TODO IB Do I need to differentiate objects tested / sent as params / being the result?
// TODO IB handle exceptions being thrown

// TODO IB !!!! maybe these are 2 objects after all
@Getter
public class MethodRunInfo {
    protected String packageName;
    protected String shortClassName;
    protected ObjectInfo objectBeingTestedInfo;
    protected String methodName;
    protected List<ObjectInfo> argumentObjectInfos;
    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;
    protected String targetObjectName;
    protected List<String> objectsInit;
    protected List<String> argumentsInlineCode;
    protected ObjectInfo resultObjectInfo;
    protected final Map<Object, String> objectNames = new HashMap<>();
    protected final Map<String, Integer> lastIndexForObjectName = new HashMap<>();

    protected MethodRunInfo() {}

    protected Map<Object, String> getObjectNames() {
        return objectNames;
    }

    protected Map<String, Integer> getLastIndexForObjectName() {
        return lastIndexForObjectName;
    }
}
