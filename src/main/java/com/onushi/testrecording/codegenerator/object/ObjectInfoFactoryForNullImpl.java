package com.onushi.testrecording.codegenerator.object;

public class ObjectInfoFactoryForNullImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject() == null) {
            return new ObjectInfo(context.getObject(), context.getObjectName(), "null", "null", true);
        }
        return null;
    }
}
