package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGenerator;
import lombok.Getter;

import java.util.*;

@Getter
public class TestGenerator {
    // TODO IB simplify this and refactor towards this being a Context
    protected ObjectCodeGenerator targetObjectCodeGenerator;
    // TODO IB !!!! these 2 should be in targetObjectCodeGenerator
    protected String packageName;
    protected String shortClassName;
    protected String methodName;
    protected List<ObjectCodeGenerator> argumentObjectCodeGenerators;
    protected ObjectCodeGenerator expectedResultObjectCodeGenerator;
    // TODO IB !!!! suspect
    protected String resultDeclareClassName;
    protected Exception expectedException;

    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;
    protected List<String> argumentsInlineCode;
    protected List<DependencyMethodRunInfo> dependencyMethodRuns = new ArrayList<>();

    protected final Map<Object, ObjectCodeGenerator> objectCodeGeneratorCache = new LinkedHashMap<>();
    protected final Map<String, Integer> lastIndexForObjectName = new HashMap<>();

    protected TestGenerator() {}

    protected Map<String, Integer> getLastIndexForObjectName() {
        return lastIndexForObjectName;
    }

    public Map<Object, ObjectCodeGenerator> getObjectCodeGeneratorCache() {
        return objectCodeGeneratorCache;
    }
}
