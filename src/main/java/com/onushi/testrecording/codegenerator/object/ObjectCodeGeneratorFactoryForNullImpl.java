package com.onushi.testrecording.codegenerator.object;

public class ObjectCodeGeneratorFactoryForNullImpl extends ObjectCodeGeneratorFactory {
    @Override
    public ObjectInfo createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject() == null) {
            return new ObjectInfo(context.getObject(), context.getObjectName(), "null", "null", true);
        }
        return null;
    }
}
