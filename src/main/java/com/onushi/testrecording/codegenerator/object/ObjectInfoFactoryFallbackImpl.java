package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

public class ObjectInfoFactoryFallbackImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectCodeGenerator(ObjectInfoCreationContext context) {
        ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName(), false);

        objectInfo.initCode = new StringGenerator()
                .setTemplate("// TODO Create this object\n" +
                        "// {{shortClassName}} {{objectName}} = new {{shortClassName}}();\n")
                .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                .addAttribute("objectName", context.getObjectName())
                .generate();

        objectInfo.requiredImports.add(context.getObject().getClass().getName());
        return objectInfo;
    }
}
