package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringService;

public class ObjectCodeGeneratorFactoryForPrimitiveImpl extends ObjectCodeGeneratorFactory {
    private StringService stringService;

    public ObjectCodeGeneratorFactoryForPrimitiveImpl(StringService stringService) {
        this.stringService = stringService;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        String fullClassName = context.getObject().getClass().getName();
        switch (fullClassName) {
            case "java.lang.Float":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject() + "f", "Float", true);
            case "java.lang.Long":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject() + "L", "Long", true);
            case "java.lang.Byte":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), "(byte)" + context.getObject(), "Byte", true);
            case "java.lang.Short":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), "(short)" + context.getObject(), "Short", true);
            case "java.lang.Character":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), "'" + context.getObject() + "'", "Char", true);
            case "java.lang.String":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), "\"" + stringService.escape(context.getObject().toString()) + "\"", "String", true);
            case "java.lang.Boolean":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject().toString(), "Boolean", true);
            case "java.lang.Integer":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject().toString(), "Integer", true);
            case "java.lang.Double":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject().toString(), "Double", true);
            default:
                return null;
        }
    }
}
