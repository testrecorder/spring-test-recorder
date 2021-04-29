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
    protected String resultDeclareClassName;
    protected Exception expectedException;

    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;
    protected List<String> objectsInit;
    protected List<String> argumentsInlineCode;
    protected List<String> expectedResultInit;

    protected final Map<Object, ObjectCodeGenerator> objectCodeGeneratorCache = new HashMap<>();
    protected final Map<String, Integer> lastIndexForObjectName = new HashMap<>();

    protected TestGenerator() {}

    protected Map<String, Integer> getLastIndexForObjectName() {
        return lastIndexForObjectName;
    }

    public Map<Object, ObjectCodeGenerator> getObjectCodeGeneratorCache() {
        return objectCodeGeneratorCache;
    }
}
