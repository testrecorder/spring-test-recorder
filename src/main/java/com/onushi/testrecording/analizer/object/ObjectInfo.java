package com.onushi.testrecording.analizer.object;

// TODO IB create a class analyzer to check object fields, setters and constructors
// TODO IB I can use serialization to transform to Json and back
// TODO IB !!!! think how to split declaration + initialization from use for complex objects
// TODO IB !!!! detect if it's inline or no
// TODO IB !!!! https://medium.com/analytics-vidhya/top-10-java-classes-from-utility-package-a4bebde7c267

public abstract class ObjectInfo {
    protected final Object object;

    protected ObjectInfo(Object object) {
        this.object = object;
    }

    public abstract String getValue();

    public abstract boolean isInline();

    public String getClassName() {
        return ObjectInfo.getClassName(object);
    }

    private static String getClassName(Object object) {
        if (object == null) {
            return "null";
        } else {
            return object.getClass().getName();
        }
    }

    public static ObjectInfo createObjectInfo(Object object) {
        String className = getClassName(object);
            switch (className) {
                case "null":
                    return new NullObjectInfo();
                case "java.lang.Float":
                    return new FloatObjectInfo(object);
                case "java.lang.Long":
                    return new LongObjectInfo(object);
                case "java.lang.Byte":
                    return new ByteObjectInfo(object);
                case "java.lang.Short":
                    return new ShortObjectInfo(object);
                case "java.lang.Character":
                    return new CharacterObjectInfo(object);
                case "java.lang.String":
                    return new StringObjectInfo(object);
                case "java.util.Date":
                    return new DateObjectInfo(object);
                case "java.lang.Boolean":
                case "java.lang.Integer":
                case "java.lang.Double":
                default:
                    return new GenericObjectInfo(object);
            }
    }
}

