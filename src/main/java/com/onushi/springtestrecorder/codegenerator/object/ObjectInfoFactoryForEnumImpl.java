package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;

import java.util.Collections;

public class ObjectInfoFactoryForEnumImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        Class<?> clazz = context.getObject().getClass();
        if (clazz.isEnum()) {
            String inlineCode = clazz.getSimpleName() + "." + context.getObject().toString();
            ObjectInfo objectInfo = new ObjectInfo(context, inlineCode);

            takeSnapshot(objectInfo, context);
            if (context.getTestGenerator().getCurrentTestRecordingPhase() != TestRecordingPhase.AFTER_METHOD_RUN) {
                objectInfo.toRunAfterMethodRun = () -> takeSnapshot(objectInfo, context);
            }

            objectInfo.declareRequiredImports.add(clazz.getName());

            return objectInfo;
        }
        return null;
    }

    // TODO IB !!!! test
    private void takeSnapshot(ObjectInfo objectInfo, ObjectInfoCreationContext context) {
        addVisiblePropertySnapshot(objectInfo, "", context.getTestGenerator().getCurrentTestRecordingPhase(),
                VisiblePropertySnapshot.builder()
                        .value(PropertyValue.fromString(objectInfo.inlineCode))
                        .requiredImports(Collections.singletonList(context.getObject().getClass().getName()))
                        .build());
    }
}
