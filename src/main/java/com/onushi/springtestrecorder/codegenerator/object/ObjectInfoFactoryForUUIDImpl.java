package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;

import java.util.Collections;

public class ObjectInfoFactoryForUUIDImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        String fullClassName = context.getObject().getClass().getName();
        if (fullClassName.equals("java.util.UUID")) {
            ObjectInfo objectInfo =
                    new ObjectInfo(context, context.getObjectName());

            objectInfo.initCode = new StringGenerator()
                    .setTemplate("UUID {{objectName}} = UUID.fromString(\"{{uuid}}\");")
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("uuid", context.getObject().toString())
                    .generate();

            String value = new StringGenerator()
                    .setTemplate("UUID.fromString(\"{{uuid}}\")")
                    .addAttribute("uuid", context.getObject().toString())
                    .generate();

            addVisiblePropertySnapshot(objectInfo, "", context.getTestGenerator().getCurrentTestRecordingPhase(),
                    VisiblePropertySnapshot.builder()
                            .value(PropertyValue.fromString(value))
                            .requiredImports(Collections.singletonList(fullClassName))
                            .build());

            objectInfo.declareRequiredImports.add("java.util.UUID");

            return objectInfo;
        }
        return null;
    }
}
