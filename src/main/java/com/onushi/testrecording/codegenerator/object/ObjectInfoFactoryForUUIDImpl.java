package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

public class ObjectInfoFactoryForUUIDImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        String fullClassName = context.getObject().getClass().getName();
        if (fullClassName.equals("java.util.UUID")) {
            ObjectInfo objectInfo =
                    new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName());

            objectInfo.initCode = new StringGenerator()
                    .setTemplate("UUID {{objectName}} = UUID.fromString(\"{{uuid}}\");")
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("uuid", context.getObject().toString())
                    .generate();

            String value = new StringGenerator()
                    .setTemplate("UUID.fromString(\"{{uuid}}\");")
                    .addAttribute("uuid", context.getObject().toString())
                    .generate();

            objectInfo.addVisibleProperty("", VisibleProperty.builder()
                    .finalValue(PropertyValue.fromString(value))
                    .build());

            objectInfo.initRequiredImports.add("java.util.UUID");

            return objectInfo;
        }
        return null;
    }
}
