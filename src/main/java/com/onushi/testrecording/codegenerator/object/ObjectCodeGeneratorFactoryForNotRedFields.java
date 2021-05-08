package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.object.FieldValueStatus;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

// TODO IB !!!! consider that all the next factories need to work with all VALUE_READ fields
public class ObjectCodeGeneratorFactoryForNotRedFields implements ObjectCodeGeneratorFactory {

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        boolean allFieldsAreRead = context.getObjectState().values().stream()
                .allMatch(x -> x.getFieldValueStatus() == FieldValueStatus.VALUE_READ);
        if (allFieldsAreRead) {
            return null;
        }

        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

        objectCodeGenerator.initCode = new StringGenerator()
                .setTemplate("// TODO Create this object\n" +
                        "// {{shortClassName}} {{objectName}} = new {{shortClassName}}();")
                .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                .addAttribute("objectName", context.getObjectName())
                .generate();

        objectCodeGenerator.requiredImports.add(context.getObject().getClass().getName());
        return objectCodeGenerator;
    }
}
