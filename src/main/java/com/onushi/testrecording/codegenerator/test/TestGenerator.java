package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.object.ObjectCodeGenerator;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TestGenerator {
    protected ObjectCodeGenerator targetObjectCodeGenerator;
    protected String packageName;
    protected String shortClassName;
    protected String methodName;
    protected List<ObjectCodeGenerator> argumentObjectCodeGenerators;
    protected ObjectCodeGenerator expectedResultObjectCodeGenerator;
    protected Class<?> resultType;
    protected Exception expectedException;

    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;
    protected List<String> objectsInit;
    protected List<String> argumentsInlineCode;
    protected String expectedResultInit;

    protected final Map<Object, String> objectNames = new HashMap<>();
    protected final Map<Object, ObjectCodeGenerator> objectObjectCodeGeneratorCache = new HashMap<>();
    protected final Map<String, Integer> lastIndexForObjectName = new HashMap<>();

    protected TestGenerator() {}

    // TODO IB !!!! remove this and use getObjectObjectCodeGeneratorCache
    protected Map<Object, String> getObjectNames() {
        return objectNames;
    }

    protected Map<String, Integer> getLastIndexForObjectName() {
        return lastIndexForObjectName;
    }

    protected Map<Object, ObjectCodeGenerator> getObjectObjectCodeGeneratorCache() {
        return objectObjectCodeGeneratorCache;
    }
}
