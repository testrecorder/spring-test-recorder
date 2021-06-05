package com.onushi.testrecording.codegenerator.object;

import java.text.SimpleDateFormat;
import java.util.*;

public class ObjectInfoFactoryForDateImpl extends ObjectInfoFactory {

    public static final String SIMPLE_DATE_FORMAT_HELPER = "SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");";

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject().getClass().getName().equals("java.util.Date")) {

            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(),
                    context.getObjectName());

            objectInfo.initRequiredImports.add("java.text.SimpleDateFormat");
            objectInfo.initRequiredImports.add("java.util.Date");

            objectInfo.initRequiredHelperObjects.add(SIMPLE_DATE_FORMAT_HELPER);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateStr = simpleDateFormat.format((Date) context.getObject());

            objectInfo.initCode = String.format("Date %s = simpleDateFormat.parse(\"%s\");", context.getObjectName(), dateStr);
            objectInfo.setCanUseDoubleEqualForComparison(false);

            objectInfo.addVisibleProperty("", VisibleProperty.builder()
                            .finalValue(PropertyValue.fromString(String.format("simpleDateFormat.parse(\"%s\")", dateStr)))
                            .requiredHelperObjects(Collections.singletonList(SIMPLE_DATE_FORMAT_HELPER))
                            .build()
                    );

            return objectInfo;
        } else {
            return null;
        }
    }
}
