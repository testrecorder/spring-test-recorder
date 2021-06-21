package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.springtestrecorder.codegenerator.object.ObjectInfo;
import lombok.Getter;

import java.util.*;

@Getter
public class TestGenerator {
    protected long threadId;
    protected TestRecordingPhase currentTestRecordingPhase = TestRecordingPhase.UNKNOWN;
    protected ObjectInfo targetObjectInfo;
    // TODO IB these 2 should be in targetObjectInfo
    protected String packageName;
    protected String shortClassName;
    protected String methodName;
    protected List<ObjectInfo> argumentObjectInfos;
    protected Class<?> fallBackResultType;

    protected ObjectInfo expectedResultObjectInfo;
    protected String resultDeclareClassName;
    protected Exception expectedException;

    protected final List<DependencyMethodRunInfo> dependencyMethodRuns = Collections.synchronizedList(new ArrayList<>());

    // TODO IB !!!! here it was LinkedHashMap
    protected final Map<Object, ObjectInfo> objectInfoCache = new IdentityHashMap<>();
    protected final Map<String, Integer> lastIndexForObjectName = new HashMap<>();
    protected final Set<Object> objectsPendingInit = new HashSet<>();

    protected TestGenerator() {}
}
