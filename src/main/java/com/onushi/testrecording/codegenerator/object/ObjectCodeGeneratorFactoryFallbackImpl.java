package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryFallbackImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;

    public ObjectCodeGeneratorFactoryFallbackImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

        StringBuilder fieldsInitCode = new StringBuilder();
        List<ObjectCodeGenerator> fieldObjectCodeGenerators = new ArrayList<>();
        for (Map.Entry<String, FieldValue> entry : context.getObjectState().entrySet()) {

            ObjectCodeGenerator fieldObjectCodeGenerator = objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), entry.getValue().getValue());
            fieldObjectCodeGenerators.add(fieldObjectCodeGenerator);
            fieldsInitCode.append(new StringGenerator()
                    .setTemplate("// {{objectName}}.{{fieldName}} = {{fieldInlineCode}};\n")
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("fieldName", entry.getKey())
                    .addAttribute("fieldInlineCode", fieldObjectCodeGenerator.getInlineCode())
                    .generate());
        }

        objectCodeGenerator.initCode = new StringGenerator()
                .setTemplate("// TODO Create this object\n" +
                        "// {{shortClassName}} {{objectName}} = new {{shortClassName}}();\n" +
                        "{{fieldsInitCode}}")
                .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                .addAttribute("objectName", context.getObjectName())
                .addAttribute("fieldsInitCode", fieldsInitCode.toString())
                .generate();

        objectCodeGenerator.dependencies = fieldObjectCodeGenerators.stream()
                .distinct()
                .collect(Collectors.toList());

        objectCodeGenerator.requiredImports.add(context.getObject().getClass().getName());
        return objectCodeGenerator;
    }
}
