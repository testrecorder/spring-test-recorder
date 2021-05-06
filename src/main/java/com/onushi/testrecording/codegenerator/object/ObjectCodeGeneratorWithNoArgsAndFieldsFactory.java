package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.analyzer.object.ObjectStateReaderService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorWithNoArgsAndFieldsFactory {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public ObjectCodeGeneratorWithNoArgsAndFieldsFactory(ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName,
                                                  TestGenerator testGenerator,
                                                  ObjectStateReaderService objectStateReaderService) {

        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, objectName);

        // TODO IB !!!! this is computed multiple times. actually I could compute dependent objects once
        Map<String, FieldValue> objectState = objectStateReaderService.getObjectState(object);

        StringBuilder fieldsInitCode = new StringBuilder();
        List<ObjectCodeGenerator> fieldObjectCodeGenerators = new ArrayList<>();
        for (Map.Entry<String, FieldValue> entry : objectState.entrySet()) {
            ObjectCodeGenerator fieldObjectCodeGenerator = objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, entry.getValue().getValue());
            fieldObjectCodeGenerators.add(fieldObjectCodeGenerator);
            fieldsInitCode.append(new StringGenerator()
                    .setTemplate("{{objectName}}.{{fieldName}} = {{fieldInlineCode}};\n")
                    .addAttribute("objectName", objectName)
                    .addAttribute("fieldName", entry.getKey())
                    .addAttribute("fieldInlineCode", fieldObjectCodeGenerator.getInlineCode())
                    .generate());
        }

        objectCodeGenerator.initCode = new StringGenerator()
                .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}();\n" +
                        "{{fieldsInitCode}}")
                .addAttribute("shortClassName", object.getClass().getSimpleName())
                .addAttribute("objectName", objectName)
                .addAttribute("fieldsInitCode", fieldsInitCode.toString())
                .generate();

        objectCodeGenerator.dependencies = fieldObjectCodeGenerators.stream()
                .distinct()
                .collect(Collectors.toList());

        objectCodeGenerator.requiredImports.add(object.getClass().getName());
        return objectCodeGenerator;
    }

}
