package com.onushi.testrecording.codegenerator.object;

import lombok.Getter;

import java.util.*;

@Getter
public class ObjectInfo {
    protected final Object object;
    protected final String objectName;
    protected final String inlineCode;
    protected String fullClassNameForDeclare;
    protected List<ObjectInfo> initDependencies = new ArrayList<>();
    // TODO IB !!!! use this for each ObjectInfoFactory and test
    protected Map<String, VisibleProperty> visibleProperties = new LinkedHashMap<>();
    protected List<String> initRequiredImports = new ArrayList<>();
    protected final List<String> initRequiredHelperObjects = new ArrayList<>();
    protected String initCode = "";
    protected boolean initAdded = false;

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
        this.fullClassNameForDeclare = object.getClass().getSimpleName();
    }

    protected ObjectInfo(Object object, String objectName, String inlineCode, String fullClassNameForDeclare) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
        this.fullClassNameForDeclare = fullClassNameForDeclare;
    }
}

