package com.onushi.springtestrecorder.codegenerator.object;

import java.util.Collections;

public class ObjectInfoFactoryForEnumImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        Class<?> clazz = context.getObject().getClass();
        if (clazz.isEnum()) {
            String inlineCode = clazz.getSimpleName() + "." + context.getObject().toString();
            ObjectInfo objectInfo = new ObjectInfo(context, inlineCode);

            addVisiblePropertySnapshot(objectInfo, "", context.getTestGenerator().getCurrentTestRecordingPhase(),
                    VisiblePropertySnapshot.builder()
                            .value(PropertyValue.fromString(inlineCode))
                            .requiredImports(Collections.singletonList(clazz.getName()))
                            .build());

            objectInfo.declareRequiredImports.add(clazz.getName());

            return objectInfo;
        }
        return null;
    }
}
