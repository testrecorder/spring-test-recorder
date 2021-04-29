package com.onushi.testrecording.codegenerator.object;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ObjectCodeGenerator {
    protected final Object object;
    protected final String objectName;
    protected final String inlineCode;
    protected List<ObjectCodeGenerator> dependencies = new ArrayList<>();
    protected List<String> requiredImports = new ArrayList<>();
    protected List<String> requiredHelperObjects = new ArrayList<>();
    protected String initCode = "";

    protected ObjectCodeGenerator(Object object, String objectName, String inlineCode) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
    }
}

