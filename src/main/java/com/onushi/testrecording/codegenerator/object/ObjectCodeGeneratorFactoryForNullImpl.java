package com.onushi.testrecording.codegenerator.object;

public class ObjectCodeGeneratorFactoryForNullImpl extends ObjectCodeGeneratorFactory {
    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject() == null) {
            return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), "null", "null", true);
        }
        return null;
    }
}
