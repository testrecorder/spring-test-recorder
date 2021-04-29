package com.onushi.testrecording.codegenerator.object;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ObjectCodeGenerator {
    protected final Object object;
    protected final String objectName;
    protected final String inlineCode;
    protected String declareClassName;
    protected List<ObjectCodeGenerator> dependencies = new ArrayList<>();
    protected List<String> requiredImports = new ArrayList<>();
    protected List<String> requiredHelperObjects = new ArrayList<>();
    protected String initCode = "";
    // these 2 are used to traverse the ObjectCodeGenerator graph
    private boolean initPrepared = false;
    private boolean initDone = false;

    public boolean isInitPrepared() {
        return initPrepared;
    }

    public boolean isInitDone() {
        return initDone;
    }

    public ObjectCodeGenerator setInitPrepared(boolean initPrepared) {
        this.initPrepared = initPrepared;
        return this;
    }

    public ObjectCodeGenerator setInitDone(boolean initDone) {
        this.initDone = initDone;
        return this;
    }

    protected ObjectCodeGenerator(Object object, String objectName, String inlineCode) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
    }

    protected ObjectCodeGenerator(Object object, String objectName, String inlineCode, String declareClassName) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
        this.declareClassName = declareClassName;
    }
}

