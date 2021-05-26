package com.onushi.testrecording.codegenerator.object;

public class ObjectCodeGeneratorFactoryForEnumImpl extends ObjectCodeGeneratorFactory {
    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        Class<?> clazz = context.getObject().getClass();
        if (clazz.isEnum()) {
            String inlineCode = clazz.getSimpleName() + "." + context.getObject().toString();
            ObjectCodeGenerator objectCodeGenerator =
                    new ObjectCodeGenerator(context.getObject(), context.getObjectName(), inlineCode);
            objectCodeGenerator.requiredImports.add(clazz.getName());

            return  objectCodeGenerator;
        }
        return null;
    }
}
