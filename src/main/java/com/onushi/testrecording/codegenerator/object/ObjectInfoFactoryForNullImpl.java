package com.onushi.testrecording.codegenerator.object;

public class ObjectInfoFactoryForNullImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject() == null) {
            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), "null", "null")
                    .setCanUseDoubleEqualForComparison(true);
            objectInfo.visibleProperties.put("", VisibleProperty.builder()
                    .finalValue("null")
                    .build());
            return objectInfo;
        }
        return null;
    }
}
