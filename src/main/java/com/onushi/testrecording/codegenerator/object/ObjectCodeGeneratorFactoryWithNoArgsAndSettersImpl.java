package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.analyzer.object.SetterInfo;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryWithNoArgsAndSettersImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectCodeGeneratorFactoryWithNoArgsAndSettersImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager,
                                                              ClassInfoService classInfoService,
                                                              ObjectStateReaderService objectStateReaderService,
                                                              ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.classInfoService = classInfoService;
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
        this.objectStateReaderService = objectStateReaderService;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject() == null) {
            return null;
        }
        if (!classInfoService.hasPublicNoArgsConstructor(context.getObject().getClass())) {
            return null;
        }
        // TODO IB !!!! duplicate code in many places
        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(context.getObject());
        Map<String, SetterInfo> settersForFields = objectCreationAnalyzerService.getSettersForFields(context.getObject(), objectState);
        if (objectState.values().size() != settersForFields.values().size()) {
            return null;
        }

        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

        StringBuilder builderSetters = new StringBuilder();
        StringBuilder simpleSetters = new StringBuilder();
        List<ObjectCodeGenerator> fieldObjectCodeGenerators = new ArrayList<>();
        List<FieldValue> sortedFields = objectState.values().stream()
                .sorted(Comparator.comparing(f -> f.getField().getName()))
                .collect(Collectors.toList());
        for (FieldValue field : sortedFields) {
            ObjectCodeGenerator fieldObjectCodeGenerator =
                    objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), field.getValue());
            fieldObjectCodeGenerators.add(fieldObjectCodeGenerator);
            SetterInfo setterInfo = settersForFields.get(field.getField().getName());
            if (setterInfo.isForBuilder()) {
                builderSetters.append(new StringGenerator()
                        .setTemplate("\n    .{{setterName}}({{fieldInlineCode}})")
                        .addAttribute("setterName", setterInfo.getName())
                        .addAttribute("fieldInlineCode", fieldObjectCodeGenerator.getInlineCode())
                        .generate());
            } else {
                simpleSetters.append(new StringGenerator()
                        .setTemplate("{{objectName}}.{{setterName}}({{fieldInlineCode}});\n")
                        .addAttribute("objectName", context.getObjectName())
                        .addAttribute("setterName", setterInfo.getName())
                        .addAttribute("fieldInlineCode", fieldObjectCodeGenerator.getInlineCode())
                        .generate());
            }
        }

        objectCodeGenerator.initCode = new StringGenerator()
                .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}()" +
                        "{{builderSetters}};\n" +
                        "{{simpleSetters}}")
                .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                .addAttribute("objectName", context.getObjectName())
                .addAttribute("builderSetters", builderSetters.toString())
                .addAttribute("simpleSetters", simpleSetters.toString())
                .generate();

        objectCodeGenerator.dependencies = fieldObjectCodeGenerators.stream()
                .distinct()
                .collect(Collectors.toList());

        objectCodeGenerator.requiredImports.add(context.getObject().getClass().getName());

        return objectCodeGenerator;
    }
}
