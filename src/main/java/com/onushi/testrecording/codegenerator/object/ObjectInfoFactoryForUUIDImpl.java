package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

public class ObjectInfoFactoryForUUIDImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectCodeGenerator(ObjectInfoCreationContext context) {
        String fullClassName = context.getObject().getClass().getName();
        if (fullClassName.equals("java.util.UUID")) {
            ObjectInfo objectInfo =
                    new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName(), false);

            objectInfo.initCode = new StringGenerator()
                    .setTemplate("UUID {{objectName}} = UUID.fromString(\"{{uuid}}\");")
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("uuid", context.getObject().toString())
                    .generate();

            objectInfo.requiredImports.add("java.util.UUID");

            return objectInfo;
        }
        return null;
    }
}
