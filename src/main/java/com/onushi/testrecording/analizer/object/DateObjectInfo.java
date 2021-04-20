package com.onushi.testrecording.analizer.object;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DateObjectInfo extends ObjectInfo {
    public DateObjectInfo(Object object) {
        super(object);
    }

    @Override
    public List<String> getRequiredIncludes() {
        return Arrays.asList(
                "import java.text.SimpleDateFormat;\n",
                "import java.util.Date;\n"
        );
    }

    @Override
    public List<String> getRequiredHelperObjects() {
        return Collections.singletonList(
                "        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\");\n"
        );
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
        // TODO IB !!!! required import
        // import java.text.SimpleDateFormat;
        // import java.util.Date;

        // TODO IB !!!! required before
        // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        // Date date1 = simpleDateFormat.parse("2021-04-19 20:50:45.457");
        // assertEquals(sampleService.toYYYY_MM_DD_T_HH_MM_SS_Z(date1), "2021-04-19T17:50:45Z");
        return "date1";
    }

    @Override
    public boolean isOnlyInline() {
        return false;
    }
}
