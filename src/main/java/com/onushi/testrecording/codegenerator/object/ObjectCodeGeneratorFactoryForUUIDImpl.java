package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

public class ObjectCodeGeneratorFactoryForUUIDImpl extends ObjectCodeGeneratorFactory {
    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        String fullClassName = context.getObject().getClass().getName();
        if (fullClassName.equals("java.util.UUID")) {
            ObjectCodeGenerator objectCodeGenerator =
                    new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName(), false);

            objectCodeGenerator.initCode = new StringGenerator()
                    .setTemplate("UUID {{objectName}} = UUID.fromString(\"{{uuid}}\");")
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("uuid", context.getObject().toString())
                    .generate();

            objectCodeGenerator.requiredImports.add("java.util.UUID");

            return  objectCodeGenerator;
        }
        return null;
    }
}
