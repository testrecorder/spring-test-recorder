package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;
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
    protected TestRecordingPhase creationPhase;
    // TODO IB !!!! 4 find a better solution. Can I move in ObjectInfoFactory
    protected Runnable toRunAfterMethodRun = null;

    // TODO IB !!!! remove all calls to addVisibleProperty
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

    protected ObjectInfo(ObjectInfoCreationContext context, String inlineCode) {
        this(context, inlineCode, context.getObject().getClass().getSimpleName());
    }

    protected ObjectInfo(ObjectInfoCreationContext context, String inlineCode, String composedClassNameForDeclare) {
        this.creationPhase = context.getTestGenerator().getCurrentTestRecordingPhase();
        this.object = context.getObject();
        this.objectName = context.getObjectName();
        this.inlineCode = inlineCode;
        this.composedClassNameForDeclare = composedClassNameForDeclare;
    }
}

