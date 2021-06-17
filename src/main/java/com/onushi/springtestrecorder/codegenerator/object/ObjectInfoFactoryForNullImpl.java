package com.onushi.springtestrecorder.codegenerator.object;

public class ObjectInfoFactoryForNullImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject() == null) {
            ObjectInfo objectInfo = new ObjectInfo(context, "null", "null");
            objectInfo.visibleProperties.put("", VisibleProperty.builder()
                    .finalValue(PropertyValue.fromString("null"))
                    .build());
            return objectInfo;
        }
        return null;
    }
}
