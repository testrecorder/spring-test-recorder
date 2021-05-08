package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryWithNoArgsAndFieldsImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectCodeGeneratorFactoryWithNoArgsAndFieldsImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager,
                                                             ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (!objectCreationAnalyzerService.canBeCreatedWithNoArgsAndFields(context.getObject())) {
            return null;
        }

        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

        StringBuilder fieldsInitCode = new StringBuilder();
        List<ObjectCodeGenerator> fieldObjectCodeGenerators = new ArrayList<>();
        for (Map.Entry<String, FieldValue> entry : context.getObjectState().entrySet()) {
            ObjectCodeGenerator fieldObjectCodeGenerator = objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), entry.getValue().getValue());
            fieldObjectCodeGenerators.add(fieldObjectCodeGenerator);
            fieldsInitCode.append(new StringGenerator()
                    .setTemplate("{{objectName}}.{{fieldName}} = {{fieldInlineCode}};\n")
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("fieldName", entry.getKey())
                    .addAttribute("fieldInlineCode", fieldObjectCodeGenerator.getInlineCode())
                    .generate());
        }

        objectCodeGenerator.initCode = new StringGenerator()
                .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}();\n" +
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
