package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryWithNoArgsAndFields {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;
    private final ObjectStateReaderService objectStateReaderService;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectCodeGeneratorFactoryWithNoArgsAndFields(ObjectCodeGeneratorFactory objectCodeGeneratorFactory,
                                                         ObjectStateReaderService objectStateReaderService,
                                                         ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
        this.objectStateReaderService = objectStateReaderService;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (objectCreationAnalyzerService.canBeCreatedWithNoArgsAndFields(context.getObject())) {

            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

            // TODO IB !!!! this is computed multiple times. actually I could compute dependent objects once
            Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(context.getObject());

            StringBuilder fieldsInitCode = new StringBuilder();
            List<ObjectCodeGenerator> fieldObjectCodeGenerators = new ArrayList<>();
            for (Map.Entry<String, FieldValue> entry : objectState.entrySet()) {
                ObjectCodeGenerator fieldObjectCodeGenerator = objectCodeGeneratorFactory.getCommonObjectCodeGenerator(context.getTestGenerator(), entry.getValue().getValue());
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
        } else {
            return null;
        }
    }

}
