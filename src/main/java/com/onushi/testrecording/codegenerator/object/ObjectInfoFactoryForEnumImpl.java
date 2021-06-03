package com.onushi.testrecording.codegenerator.object;

public class ObjectInfoFactoryForEnumImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectCodeGenerator(ObjectInfoCreationContext context) {
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
