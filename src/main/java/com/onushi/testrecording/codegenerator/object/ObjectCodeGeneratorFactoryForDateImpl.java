package com.onushi.testrecording.codegenerator.object;

import java.text.SimpleDateFormat;
import java.util.*;

public class ObjectCodeGeneratorFactoryForDateImpl extends ObjectCodeGeneratorFactory {
    @Override
    public ObjectInfo createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject().getClass().getName().equals("java.util.Date")) {

            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(),
                    context.getObjectName(), false);

            objectInfo.requiredImports.add("java.text.SimpleDateFormat");
            objectInfo.requiredImports.add("java.util.Date");

            objectInfo.requiredHelperObjects.add("SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateStr = simpleDateFormat.format((Date) context.getObject());

            objectInfo.initCode = String.format("Date %s = simpleDateFormat.parse(\"%s\");", context.getObjectName(), dateStr);
            return objectInfo;
        } else {
            return null;
        }
    }
}
