package com.onushi.testrecording.generator.object;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateObjectCodeGenerator extends ObjectCodeGenerator {
    public DateObjectCodeGenerator(Object object, String objectName) {
        super(object, objectName, false, objectName);

        this.requiredImports.add("java.text.SimpleDateFormat");
        this.requiredImports.add("java.util.Date");

        this.requiredHelperObjects.add("SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = simpleDateFormat.format((Date)object);

        this.initCode = String.format("Date %s = simpleDateFormat.parse(\"%s\");", this.objectName, dateStr);
    }
}
