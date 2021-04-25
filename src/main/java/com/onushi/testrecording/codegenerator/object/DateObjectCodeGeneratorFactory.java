package com.onushi.testrecording.codegenerator.object;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateObjectCodeGeneratorFactory {
    public ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, false, objectName);

        objectCodeGenerator.requiredImports.add("java.text.SimpleDateFormat");
        objectCodeGenerator.requiredImports.add("java.util.Date");

        objectCodeGenerator.requiredHelperObjects.add("SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = simpleDateFormat.format((Date)object);

        objectCodeGenerator.initCode = String.format("Date %s = simpleDateFormat.parse(\"%s\");", objectName, dateStr);
        return objectCodeGenerator;
    }
}
