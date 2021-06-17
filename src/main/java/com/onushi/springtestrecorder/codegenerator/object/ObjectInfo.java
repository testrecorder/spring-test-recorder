package com.onushi.springtestrecorder.codegenerator.object;

import lombok.Getter;

import java.util.*;

@Getter
public class ObjectInfo {
    protected final Object object;
    protected final String objectName;
    protected final String inlineCode;
    protected String composedClassNameForDeclare;
    protected List<ObjectInfo> initDependencies = new ArrayList<>();
    protected final Map<String, VisibleProperty> visibleProperties = new LinkedHashMap<>();
    protected List<String> declareRequiredImports = new ArrayList<>();
    protected List<String> initRequiredImports = new ArrayList<>();
    protected final List<String> initRequiredHelperObjects = new ArrayList<>();
    protected String initCode = "";
    protected boolean initAdded = false;
    // TODO IB find a better solution
    protected Runnable toRunAfterMethodRun = null;

    protected ObjectInfo addVisibleProperty(String key, VisibleProperty visibleProperty) {
        this.visibleProperties.put(key, visibleProperty);
        return this;
    }

    public void setInitAdded(boolean initAdded) {
        this.initAdded = initAdded;
    }

    public boolean hasInitCode() {
        return !initCode.equals("");
    }

    protected ObjectInfo(Object object, String objectName, String inlineCode) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
        this.composedClassNameForDeclare = object.getClass().getSimpleName();
    }

    protected ObjectInfo(Object object, String objectName, String inlineCode, String composedClassNameForDeclare) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
        this.composedClassNameForDeclare = composedClassNameForDeclare;
    }
}

