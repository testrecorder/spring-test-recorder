package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

public class ObjectCodeGeneratorWithNoArgsConstructorFactory {
    ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        String inlineCode = new StringGenerator()
                .setTemplate("new {{shortClassName}}()")
                .addAttribute("shortClassName", object.getClass().getSimpleName())
                .generate();


        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, inlineCode);
        objectCodeGenerator.requiredImports.add(object.getClass().getName());
        return objectCodeGenerator;
    }
}
