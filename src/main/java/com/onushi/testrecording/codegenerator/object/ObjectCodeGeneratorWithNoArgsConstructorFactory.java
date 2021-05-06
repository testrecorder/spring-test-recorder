package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

public class ObjectCodeGeneratorWithNoArgsConstructorFactory {
    ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, objectName);
        objectCodeGenerator.initCode = new StringGenerator()
                .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}();")
                .addAttribute("shortClassName", object.getClass().getSimpleName())
                .addAttribute("objectName", objectName)
                .generate();

        objectCodeGenerator.requiredImports.add(object.getClass().getName());
        return objectCodeGenerator;
    }
}
