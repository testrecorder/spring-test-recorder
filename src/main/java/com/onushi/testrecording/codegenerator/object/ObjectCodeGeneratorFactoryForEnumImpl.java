package com.onushi.testrecording.codegenerator.object;

public class ObjectCodeGeneratorFactoryForEnumImpl extends ObjectCodeGeneratorFactory {
    @Override
    public ObjectInfo createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        Class<?> clazz = context.getObject().getClass();
        if (clazz.isEnum()) {
            String inlineCode = clazz.getSimpleName() + "." + context.getObject().toString();
            ObjectInfo objectInfo =
                    new ObjectInfo(context.getObject(), context.getObjectName(), inlineCode, true);
            objectInfo.requiredImports.add(clazz.getName());

            return objectInfo;
        }
        return null;
    }
}
