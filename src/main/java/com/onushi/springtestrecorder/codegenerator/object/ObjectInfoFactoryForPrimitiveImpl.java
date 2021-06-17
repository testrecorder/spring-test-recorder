package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.template.StringService;

public class ObjectInfoFactoryForPrimitiveImpl extends ObjectInfoFactory {
    private final StringService stringService;

    public ObjectInfoFactoryForPrimitiveImpl(StringService stringService) {
        this.stringService = stringService;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        String fullClassName = context.getObject().getClass().getName();
        ObjectInfo objectInfo = getObjectInfo(context, fullClassName);
        if (objectInfo != null) {
            return objectInfo.addVisibleProperty("", VisibleProperty.builder()
                    .finalValue(PropertyValue.fromString(objectInfo.inlineCode))
                    .build());
        }
        return null;
    }

    private ObjectInfo getObjectInfo(ObjectInfoCreationContext context, String fullClassName) {
        switch (fullClassName) {
            case "java.lang.Float":
                return new ObjectInfo(context, context.getObject() + "f", "Float");
            case "java.lang.Long":
                return new ObjectInfo(context, context.getObject() + "L", "Long");
            case "java.lang.Byte":
                return new ObjectInfo(context, "(byte)" + context.getObject(), "Byte");
            case "java.lang.Short":
                return new ObjectInfo(context, "(short)" + context.getObject(), "Short");
            case "java.lang.Character":
                return new ObjectInfo(context, "'" + context.getObject() + "'", "Char");
            case "java.lang.String":
                return new ObjectInfo(context, "\"" + stringService.escape(context.getObject().toString()) + "\"", "String");
            case "java.lang.Boolean":
                return new ObjectInfo(context, context.getObject().toString(), "Boolean");
            case "java.lang.Integer":
                return new ObjectInfo(context, context.getObject().toString(), "Integer");
            case "java.lang.Double":
                return new ObjectInfo(context, context.getObject().toString(), "Double");
            default:
                return null;
        }
    }
}
