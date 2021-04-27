package com.onushi.testrecording.codegenerator.object;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ObjectCodeGenerator {
    protected final Object object;
    protected final String objectName;
    protected List<ObjectCodeGenerator> dependencies = new ArrayList<>();
    protected List<String> requiredImports = new ArrayList<>();
    protected List<String> requiredHelperObjects = new ArrayList<>();
    protected String initCode = "";
    protected String inlineCode;
    protected boolean isOnlyInline;

    protected ObjectCodeGenerator(Object object, String objectName, boolean isOnlyInline, String inlineCode) {
        this.object = object;
        this.objectName = objectName;
        this.isOnlyInline = isOnlyInline;
        this.inlineCode = inlineCode;
    }
}

