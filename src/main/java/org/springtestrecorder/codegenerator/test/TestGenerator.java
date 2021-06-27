/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import org.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import org.springtestrecorder.codegenerator.object.ObjectInfo;
import lombok.Getter;

import java.util.*;

@Getter
public class TestGenerator {
    protected long threadId;
    protected TestRecordingPhase currentTestRecordingPhase = TestRecordingPhase.BEFORE_METHOD_RUN;
    protected ObjectInfo targetObjectInfo;
    protected String packageName;
    protected String shortClassName;
    protected String methodName;
    protected List<ObjectInfo> argumentObjectInfos;
    protected Class<?> fallBackResultType;

    protected ObjectInfo expectedResultObjectInfo;
    protected String resultDeclareClassName;
    protected Exception expectedException;

    protected final List<DependencyMethodRunInfo> dependencyMethodRuns = Collections.synchronizedList(new ArrayList<>());

    protected final Map<Object, ObjectInfo> objectInfoCache = new IdentityHashMap<>();
    protected final Map<String, Integer> lastIndexForObjectName = new HashMap<>();
    protected final Set<Object> objectsPendingInit = new HashSet<>();

    protected TestGenerator() {}
}
