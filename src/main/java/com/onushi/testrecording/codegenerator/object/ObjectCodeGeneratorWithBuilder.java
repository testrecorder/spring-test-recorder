package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

// TODO IB !!!! with builder
public class ObjectCodeGeneratorWithBuilder extends ObjectCodeGenerator {
    protected ObjectCodeGeneratorWithBuilder(Object object, String objectName,
                                             String packageName, String shortClassName) {
        super(object, objectName, false, objectName);

        this.requiredImports.add(packageName);

        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate(
                "{{shortClassName}} {{objectName}} = {{shortClassName}}.builder()\n" +
                "    .firstName(\"firstName\")\n" +
                "    .lastName(\"lastName\")\n" +
                "    .age(30)\n" +
                "    .build();");
        stringGenerator.addAttribute("shortClassName", shortClassName);
        stringGenerator.addAttribute("objectName", objectName);

        this.initCode = stringGenerator.generate();



    }
}
