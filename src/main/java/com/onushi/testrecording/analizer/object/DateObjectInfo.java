package com.onushi.testrecording.analizer.object;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateObjectInfo extends ObjectInfo {
    public DateObjectInfo(Object object) {
        super(object);
    }

    @Override
    public String getValue() {
        Date date = (Date) object;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String dateStr = simpleDateFormat.format(date);
        return String.format("new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\").parse(\"%s\")", dateStr);
    }

    @Override
    public boolean isInline() {
        return false;
    }
}
