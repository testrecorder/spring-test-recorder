package com.onushi.testrecording.codegenerator.object;

// TODO IB !!!! with builder
public class ObjectCodeGeneratorWithBuilder extends ObjectCodeGenerator {
    protected ObjectCodeGeneratorWithBuilder(Object object, String objectName) {
        super(object, objectName, true, object.toString());
    }
}
