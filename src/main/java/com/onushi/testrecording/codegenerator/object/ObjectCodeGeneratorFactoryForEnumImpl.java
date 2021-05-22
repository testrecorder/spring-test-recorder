package com.onushi.testrecording.codegenerator.object;

public class ObjectCodeGeneratorFactoryForEnumImpl implements ObjectCodeGeneratorFactory {
    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        Class<?> clazz = context.getObject().getClass();
        if (clazz.isEnum()) {
            String inlineCode = clazz.getSimpleName() + "." + context.getObject().toString();
            ObjectCodeGenerator objectCodeGenerator =
                    new ObjectCodeGenerator(context.getObject(), context.getObjectName(), inlineCode, clazz.getSimpleName());
            objectCodeGenerator.requiredImports.add(clazz.getName());

            return  objectCodeGenerator;
        }
        return null;
    }
}