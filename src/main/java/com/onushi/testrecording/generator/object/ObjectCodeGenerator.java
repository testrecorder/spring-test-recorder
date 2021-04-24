package com.onushi.testrecording.generator.object;


// TODO IB https://medium.com/analytics-vidhya/top-10-java-classes-from-utility-package-a4bebde7c267

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

// TODO IB if needed object initialisation code could be learned from github
@Getter
public class ObjectCodeGenerator {
    protected final Object object;
    protected final String objectName;
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

