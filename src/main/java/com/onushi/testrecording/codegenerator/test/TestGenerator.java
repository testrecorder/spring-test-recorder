package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import lombok.Getter;

import java.util.*;

@Getter
public class TestGenerator {
    // TODO IB simplify this and refactor towards this being a Context
    protected ObjectInfo targetObjectInfo;
    // TODO IB these 2 should be in targetObjectInfo
    protected String packageName;
    protected String shortClassName;
    protected String methodName;
    protected List<ObjectInfo> argumentObjectInfos;
    protected ObjectInfo expectedResultObjectInfo;
    protected String resultDeclareClassName;
    protected Exception expectedException;

    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;
    protected List<String> argumentsInlineCode;
    protected List<DependencyMethodRunInfo> dependencyMethodRuns = new ArrayList<>();

    protected final Map<Object, ObjectInfo> objectInfoCache = new LinkedHashMap<>();
    protected final Map<String, Integer> lastIndexForObjectName = new HashMap<>();

    protected TestGenerator() {}

    protected Map<String, Integer> getLastIndexForObjectName() {
        return lastIndexForObjectName;
    }

    public Map<Object, ObjectInfo> getObjectInfoCache() {
        return objectInfoCache;
    }
}
