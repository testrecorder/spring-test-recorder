package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringService;

public class ObjectInfoFactoryForPrimitiveImpl extends ObjectInfoFactory {
    private StringService stringService;

    public ObjectInfoFactoryForPrimitiveImpl(StringService stringService) {
        this.stringService = stringService;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        String fullClassName = context.getObject().getClass().getName();
        switch (fullClassName) {
            case "java.lang.Float":
                return new ObjectInfo(context.getObject(), context.getObjectName(), context.getObject() + "f", "Float")
                        .setCanUseDoubleEqualForComparison(true);
            case "java.lang.Long":
                return new ObjectInfo(context.getObject(), context.getObjectName(), context.getObject() + "L", "Long")
                        .setCanUseDoubleEqualForComparison(true);
            case "java.lang.Byte":
                return new ObjectInfo(context.getObject(), context.getObjectName(), "(byte)" + context.getObject(), "Byte")
                        .setCanUseDoubleEqualForComparison(true);
            case "java.lang.Short":
                return new ObjectInfo(context.getObject(), context.getObjectName(), "(short)" + context.getObject(), "Short")
                        .setCanUseDoubleEqualForComparison(true);
            case "java.lang.Character":
                return new ObjectInfo(context.getObject(), context.getObjectName(), "'" + context.getObject() + "'", "Char")
                        .setCanUseDoubleEqualForComparison(true);
            case "java.lang.String":
                return new ObjectInfo(context.getObject(), context.getObjectName(), "\"" + stringService.escape(context.getObject().toString()) + "\"", "String")
                        .setCanUseDoubleEqualForComparison(true);
            case "java.lang.Boolean":
                return new ObjectInfo(context.getObject(), context.getObjectName(), context.getObject().toString(), "Boolean")
                        .setCanUseDoubleEqualForComparison(true);
            case "java.lang.Integer":
                return new ObjectInfo(context.getObject(), context.getObjectName(), context.getObject().toString(), "Integer")
                        .setCanUseDoubleEqualForComparison(true);
            case "java.lang.Double":
                return new ObjectInfo(context.getObject(), context.getObjectName(), context.getObject().toString(), "Double")
                        .setCanUseDoubleEqualForComparison(true);
            default:
                return null;
        }
    }
}
