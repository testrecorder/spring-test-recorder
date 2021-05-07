package com.onushi.testrecording.codegenerator.object;

public class ObjectCodeGeneratorFactoryForPrimitiveImpl implements ObjectCodeGeneratorFactory {
    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        String fullClassName = context.getObject().getClass().getName();
        switch (fullClassName) {
            case "java.lang.Float":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject() + "f", "float");
            case "java.lang.Long":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject() + "L", "long");
            case "java.lang.Byte":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), "(byte)" + context.getObject(), "byte");
            case "java.lang.Short":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), "(short)" + context.getObject(), "short");
            case "java.lang.Character":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), "'" + context.getObject() + "'", "char");
            case "java.lang.String":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), "\"" + context.getObject() + "\"", "String");
            case "java.lang.Boolean":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject().toString(), "boolean");
            case "java.lang.Integer":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject().toString(), "int");
            case "java.lang.Double":
                return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObject().toString(), "double");
            default:
                return null;
        }
    }
}
