package com.onushi.testrecording.codegenerator.object;

import lombok.Getter;

import java.util.*;

@Getter
public class ObjectInfo {
    protected final Object object;
    protected final String objectName;
    protected final String inlineCode;
    // this contains more info than the object.getClass() for generics
    protected String actualClassName;
    protected List<ObjectInfo> initDependencies = new ArrayList<>();
    // TODO IB !!!! use this for each ObjectInfoFactory and test
    protected Map<String, VisibleProperty> visibleProperties = new LinkedHashMap<>();
    protected List<String> initRequiredImports = new ArrayList<>();
    protected final List<String> initRequiredHelperObjects = new ArrayList<>();
    protected String initCode = "";
    protected boolean initAdded = false;

    // TODO IB !!!! needed??? maybe when result == arg1 or result.date == date1 ???
    protected boolean canUseDoubleEqualForComparison = false;

    protected ObjectInfo setCanUseDoubleEqualForComparison(boolean canUseDoubleEqualForComparison) {
        this.canUseDoubleEqualForComparison = canUseDoubleEqualForComparison;
        return this;
    }

    // TODO IB !!!! needed like this? it's not like others
    protected ObjectInfo addVisibleProperty(String key, VisibleProperty visibleProperty) {
        this.visibleProperties.put(key, visibleProperty);
        return this;
    }

    public ObjectInfo setInitAdded(boolean initAdded) {
        this.initAdded = initAdded;
        return this;
    }

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

