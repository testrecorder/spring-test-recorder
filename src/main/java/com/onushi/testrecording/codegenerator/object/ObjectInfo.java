package com.onushi.testrecording.codegenerator.object;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ObjectInfo {
    protected final Object object;
    protected final String objectName;
    protected final String inlineCode;
    // this contains more info than the object.getClass() for generics
    protected String actualClassName;
    protected List<ObjectInfo> elements = new ArrayList<>();
    protected List<ObjectInfo> dependencies = new ArrayList<>();
    protected List<String> requiredImports = new ArrayList<>();
    protected final List<String> requiredHelperObjects = new ArrayList<>();
    protected String initCode = "";
    // these 2 are used to traverse the ObjectInfo graph
    private boolean initPrepared = false;
    private boolean initDone = false;
    protected boolean canUseDoubleEqualForComparison;

    public void setInitPrepared(boolean initPrepared) {
        this.initPrepared = initPrepared;
    }

    public void setInitDone(boolean initDone) {
        this.initDone = initDone;
    }

    protected ObjectInfo(Object object, String objectName, String inlineCode, boolean canUseDoubleEqualForComparison) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
        this.actualClassName = object.getClass().getSimpleName();
        this.canUseDoubleEqualForComparison = canUseDoubleEqualForComparison;
    }

    protected ObjectInfo(Object object, String objectName, String inlineCode, String actualClassName, boolean canUseDoubleEqualForComparison) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
        this.actualClassName = actualClassName;
        this.canUseDoubleEqualForComparison = canUseDoubleEqualForComparison;
    }
}

