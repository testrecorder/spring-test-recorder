package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;

public class ObjectInfoFactoryForNullImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject() == null) {
            ObjectInfo objectInfo = new ObjectInfo(context, "null", "null");

            takeSnapshot(objectInfo, context);
            if (context.getTestGenerator().getCurrentTestRecordingPhase() != TestRecordingPhase.AFTER_METHOD_RUN) {
                objectInfo.toRunAfterMethodRun = () -> takeSnapshot(objectInfo, context);
            }

            return objectInfo;
        }
        return null;
    }

    private void takeSnapshot(ObjectInfo objectInfo, ObjectInfoCreationContext context) {
        addVisiblePropertySnapshot(objectInfo, "", context.getTestGenerator().getCurrentTestRecordingPhase(),
                VisiblePropertySnapshot.builder()
                        .value(PropertyValue.fromString("null"))
                        .build());
    }
}
