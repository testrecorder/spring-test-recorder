package com.onushi.testrecording.codegenerator.object;

public class ObjectCodeGeneratorFactoryForNullImpl {
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject() == null) {
            return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), "null", "null");
        }
        return null;
    }
}
