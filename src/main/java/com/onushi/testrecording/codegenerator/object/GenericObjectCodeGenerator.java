package com.onushi.testrecording.codegenerator.object;

// TODO IB !!!! with builder
public class GenericObjectCodeGenerator extends ObjectCodeGenerator {
    protected GenericObjectCodeGenerator(Object object, String objectName) {
        super(object, objectName, true, object.toString());
    }
}
