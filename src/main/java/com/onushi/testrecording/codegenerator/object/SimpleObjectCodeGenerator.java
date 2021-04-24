package com.onushi.testrecording.codegenerator.object;

public class SimpleObjectCodeGenerator extends ObjectCodeGenerator {
    protected SimpleObjectCodeGenerator(Object object, String objectName, String inlineCode) {
        super(object, objectName, true, inlineCode);
    }
}
