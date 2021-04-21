package com.onushi.testrecording.analizer.object;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateObjectInfo extends ObjectInfo {
    public DateObjectInfo(Object object, String objectName) {
        super(object, objectName, false, objectName);

        this.requiredImports = new ArrayList<>();
        this.requiredImports.add("java.text.SimpleDateFormat");
        this.requiredImports.add("java.util.Date");

        this.requiredHelperObjects = new ArrayList<>();
        this.requiredHelperObjects.add("SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");");

        Date date = (Date) object;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = simpleDateFormat.format(date);

        this.init = String.format("Date %s = simpleDateFormat.parse(\"%s\");", this.objectName, dateStr);
    }
}
