package com.onushi.springtestrecorder.codegenerator.object;

public class ObjectInfoFactoryForNullImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject() == null) {
            ObjectInfo objectInfo = new ObjectInfo(context, "null", "null");
            // TODO IB obsolete
            objectInfo.visibleProperties.put("", VisibleProperty.builder()
                    .finalValue(PropertyValue.fromString("null"))
                    .build());
            addVisiblePropertySnapshot(objectInfo, "", context.getTestGenerator().getCurrentTestRecordingPhase(),
                    VisiblePropertySnapshot.builder()
                            .value(PropertyValue.fromString("null"))
                            .build());

            return objectInfo;
        }
        return null;
    }
}
