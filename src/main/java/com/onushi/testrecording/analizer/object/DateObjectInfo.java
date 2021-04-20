package com.onushi.testrecording.analizer.object;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateObjectInfo extends ObjectInfo {
    public DateObjectInfo(Object object, String objectName) {
        super(object, objectName);
    }

    @Override
    public List<String> getRequiredImports() {
        List<String> results = new ArrayList<>();
        results.add("java.text.SimpleDateFormat");
        results.add("java.util.Date");
        return results;
    }

    @Override
    public List<String> getRequiredHelperObjects() {
        List<String> results = new ArrayList<>();
        results.add("SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");");
        return results;
    }

    @Override
    public String getInit() {
        Date date = (Date) object;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = simpleDateFormat.format(date);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Date %s = simpleDateFormat.parse(\"%s\");", this.objectName, dateStr));
        return stringBuilder.toString();
    }

    @Override
    public String getInlineCode() {
        return this.objectName;
    }

    @Override
    public boolean isOnlyInline() {
        return false;
    }
}
