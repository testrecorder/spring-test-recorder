package com.onushi.testrecording.analizer.object;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateObjectInfo extends ObjectInfo {
    public DateObjectInfo(Object object) {
        super(object);
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
        stringBuilder.append(String.format("        Date date1 = simpleDateFormat.parse(\"%s\");%n", dateStr));
        return stringBuilder.toString();
    }

    @Override
    public String getInlineCode() {
        // TODO IB !!!! required import improve. should be in TestRun
        // TODO IB !!!! required before improve. should be in TestRun
        // TODO IB !!!! TestRun should aggregate things from the ObjectInfos
        // TODO IB !!!! this should be provided by TestRun
        return "date1";
    }

    @Override
    public boolean isOnlyInline() {
        return false;
    }
}
