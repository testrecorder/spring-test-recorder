package com.onushi.testrecording.codegenerator.object;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ObjectInfo {
    protected final Object object;
    protected final String objectName;
    protected final String inlineCode;
    // this contains more info than the object.getClass() for generics
    protected String actualClassName;
    // TOD IB remove elements
    protected List<ObjectInfo> elements = new ArrayList<>();
    protected List<ObjectInfo> initDependencies = new ArrayList<>();
    // TODO IB !!!! use this for each ObjectInfoFactory and test
    protected Map<String, VisibleProperty> visibleProperties = new HashMap<>();
    protected List<String> requiredImports = new ArrayList<>();
    protected final List<String> requiredHelperObjects = new ArrayList<>();
    protected String initCode = "";
    // these 2 are used to traverse the ObjectInfo graph
    private boolean initPrepared = false;
    private boolean initDone = false;
    protected boolean canUseDoubleEqualForComparison = false;

    public ObjectInfo setInitPrepared(boolean initPrepared) {
        this.initPrepared = initPrepared;
        return this;
    }

    public ObjectInfo setInitDone(boolean initDone) {
        this.initDone = initDone;
        return this;
    }

    protected ObjectInfo setCanUseDoubleEqualForComparison(boolean canUseDoubleEqualForComparison) {
        this.canUseDoubleEqualForComparison = canUseDoubleEqualForComparison;
        return this;
    }

    protected ObjectInfo addVisibleProperty(String key, VisibleProperty visibleProperty) {
        this.visibleProperties.put(key, visibleProperty);
        return this;
    }

    // TODO IB !!!! I should have setters for other props too

    protected ObjectInfo(Object object, String objectName, String inlineCode) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
        this.actualClassName = object.getClass().getSimpleName();
    }

    protected ObjectInfo(Object object, String objectName, String inlineCode, String actualClassName) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
        this.actualClassName = actualClassName;
    }
}

