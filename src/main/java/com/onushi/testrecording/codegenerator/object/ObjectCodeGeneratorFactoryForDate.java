package com.onushi.testrecording.codegenerator.object;

import java.text.SimpleDateFormat;
import java.util.*;

public class ObjectCodeGeneratorFactoryForDate {
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject().getClass().getName().equals("java.util.Date")) {

            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(),
                    context.getObjectName(), "Date");

            objectCodeGenerator.requiredImports.add("java.text.SimpleDateFormat");
            objectCodeGenerator.requiredImports.add("java.util.Date");

            objectCodeGenerator.requiredHelperObjects.add("SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String dateStr = simpleDateFormat.format((Date) context.getObject());

            objectCodeGenerator.initCode = String.format("Date %s = simpleDateFormat.parse(\"%s\");", context.getObjectName(), dateStr);
            return objectCodeGenerator;
        } else {
            return null;
        }
    }
}
