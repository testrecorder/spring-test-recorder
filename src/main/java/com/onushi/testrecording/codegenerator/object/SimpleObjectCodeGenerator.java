package com.onushi.testrecording.codegenerator.object;

// TODO IB !!!! this should not extend ObjectCodeGenerator but be a factory
public class SimpleObjectCodeGenerator extends ObjectCodeGenerator {
    protected SimpleObjectCodeGenerator(Object object, String objectName, String inlineCode) {
        super(object, objectName, true, inlineCode);
    }
}
