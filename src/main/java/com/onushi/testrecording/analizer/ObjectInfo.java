package com.onushi.testrecording.analizer;

import java.text.SimpleDateFormat;
import java.util.Date;

// TODO IB make it work in all the simple cases
// TODO IB create a class analyzer to check object fields, setters and constructors
// TODO IB something special for char -> (char)c
// TODO IB I can use serialization to transform to Json and back

public class ObjectInfo {
    private final Object object;
    private String value = null;

    public ObjectInfo(Object object) {
        this.object = object;
    }

    public String getClassName() {
        if (object == null) {
            return null;
        } else {
            return object.getClass().getName();
        }
    }

    public String getValue() {
        if (value == null) {
            value = getValueInternal();
        }
        return value;
    }

    private String getValueInternal() {
        String className = getClassName();
        if (object == null) {
            return "null";
        }
        switch (className) {
            case "java.lang.Float":
                return object + "f";
            case "java.lang.Long":
                return object + "L";
            case "java.lang.byte":
                return "(byte)" + object;
            case "java.lang.short":
                return "(short)" + object;
            case "java.lang.String":
                return "\"" + object + "\"";
            case "java.util.Date":
                Date date = (Date) object;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String dateStr = simpleDateFormat.format(date);
                return String.format("new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\").parse(\"%s\")", dateStr);
            default:
                return object.toString();
        }
    }
}

