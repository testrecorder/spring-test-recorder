/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.template.StringService;
import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;

public class ObjectInfoFactoryForPrimitiveImpl extends ObjectInfoFactory {
    private final StringService stringService;

    public ObjectInfoFactoryForPrimitiveImpl(StringService stringService) {
        this.stringService = stringService;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        String fullClassName = context.getObject().getClass().getName();
        ObjectInfo objectInfo = getObjectInfo(context, fullClassName);
        if (objectInfo != null) {
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
                        .value(PropertyValue.fromString(objectInfo.inlineCode))
                        .build());
    }

    private ObjectInfo getObjectInfo(ObjectInfoCreationContext context, String fullClassName) {
        switch (fullClassName) {
            case "java.lang.Float":
                return new ObjectInfo(context, context.getObject() + "f", "Float");
            case "java.lang.Long":
                return new ObjectInfo(context, context.getObject() + "L", "Long");
            case "java.lang.Byte":
                return new ObjectInfo(context, "(byte)" + context.getObject(), "Byte");
            case "java.lang.Short":
                return new ObjectInfo(context, "(short)" + context.getObject(), "Short");
            case "java.lang.Character":
                return new ObjectInfo(context, "'" + context.getObject() + "'", "Char");
            case "java.lang.String":
                return new ObjectInfo(context, "\"" + stringService.escape(context.getObject().toString()) + "\"", "String");
            case "java.lang.Boolean":
                return new ObjectInfo(context, context.getObject().toString(), "Boolean");
            case "java.lang.Integer":
                return new ObjectInfo(context, context.getObject().toString(), "Integer");
            case "java.lang.Double":
                return new ObjectInfo(context, context.getObject().toString(), "Double");
            default:
                return null;
        }
    }
}
