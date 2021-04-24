package com.onushi.testrecording.generator.object;

public class GenericObjectCodeGenerator extends ObjectCodeGenerator {
    protected GenericObjectCodeGenerator(Object object, String objectName) {
        super(object, objectName, true, object.toString());
    }
}
