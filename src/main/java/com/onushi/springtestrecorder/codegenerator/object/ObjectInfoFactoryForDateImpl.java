package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;

import java.text.SimpleDateFormat;
import java.util.*;

public class ObjectInfoFactoryForDateImpl extends ObjectInfoFactory {

    public static final String SIMPLE_DATE_FORMAT_HELPER = "SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");";

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject().getClass().getName().equals("java.util.Date")) {

            ObjectInfo objectInfo = new ObjectInfo(context, context.getObjectName());

            objectInfo.declareRequiredImports.add("java.util.Date");
            objectInfo.initRequiredImports.add("java.text.SimpleDateFormat");

            objectInfo.initRequiredHelperObjects.add(SIMPLE_DATE_FORMAT_HELPER);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateStr = simpleDateFormat.format((Date) context.getObject());

            objectInfo.initCode = String.format("Date %s = simpleDateFormat.parse(\"%s\");\n", context.getObjectName(), dateStr);

            takeSnapshot(objectInfo, context);

            if (context.getTestGenerator().getCurrentTestRecordingPhase() != TestRecordingPhase.AFTER_METHOD_RUN) {
                objectInfo.toRunAfterMethodRun = () -> takeSnapshot(objectInfo, context);
            }

            return objectInfo;
        } else {
            return null;
        }
    }

    void takeSnapshot(ObjectInfo objectInfo, ObjectInfoCreationContext context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = simpleDateFormat.format((Date) context.getObject());
        addVisiblePropertySnapshot(objectInfo, "", context.getTestGenerator().getCurrentTestRecordingPhase(),
                VisiblePropertySnapshot.builder()
                        .value(PropertyValue.fromString(String.format("simpleDateFormat.parse(\"%s\")", dateStr)))
                        .requiredImports(Collections.singletonList("java.text.SimpleDateFormat"))
                        .requiredHelperObjects(Collections.singletonList(SIMPLE_DATE_FORMAT_HELPER))
                        .build());
    }
}
