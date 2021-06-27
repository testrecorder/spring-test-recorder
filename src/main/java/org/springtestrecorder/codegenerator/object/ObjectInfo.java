/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.object;

import lombok.Getter;

import java.util.*;

@Getter
public class ObjectInfo {
    public static final String CYCLIC_OBJECT_REPLACEMENT = "CYCLIC_OBJECT_REPLACEMENT";

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
        this.object = context.getObject();
        this.objectName = context.getObjectName();
        this.inlineCode = inlineCode;
        this.composedClassNameForDeclare = composedClassNameForDeclare;
    }

    @Override
    public String toString() {
        return "ObjectInfo{" +
                "objectName='" + objectName + '\'' +
                '}';
    }
}

