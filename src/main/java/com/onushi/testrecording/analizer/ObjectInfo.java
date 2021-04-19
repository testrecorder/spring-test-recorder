package com.onushi.testrecording.analizer;

import java.text.SimpleDateFormat;
import java.util.Date;

// TODO IB create a class analyzer to check object fields, setters and constructors
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

    // TODO IB !!!! detect if it's inline or no
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
            case "java.lang.Byte":
                return "(byte)" + object;
            case "java.lang.Short":
                return "(short)" + object;
            case "java.lang.Character":
                return "'" + object + "'";
            case "java.lang.String":
                return "\"" + object + "\"";
            case "java.util.Date":
                // TODO IB !!!! think how to split declaration + initialization from use for complex objects
                Date date = (Date) object;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String dateStr = simpleDateFormat.format(date);
                return String.format("new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss.SSS\").parse(\"%s\")", dateStr);
            case "java.lang.Boolean":
            case "java.lang.Integer":
            case "java.lang.Double":
            default:
                return object.toString();
        }
    }
}

