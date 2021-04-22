package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO IB !!!! Move some parts of MethodRunInfo here
// TODO IB !!!! Read all the code again to see if it makes sense
// TODO IB !!!! Write tests for all the code
@Getter
public class TestGenInfo {
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

    protected TestGenInfo() {}


    protected Map<Object, String> getObjectNames() {
        return objectNames;
    }

    protected Map<String, Integer> getLastIndexForObjectName() {
        return lastIndexForObjectName;
    }
}
