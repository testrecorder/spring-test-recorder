package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectInfoFactoryWithNoArgsAndFieldsImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectInfoFactoryWithNoArgsAndFieldsImpl(ObjectInfoFactoryManager objectInfoFactoryManager,
                                                    ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.objectInfoFactoryManager = objectInfoFactoryManager;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (!objectCreationAnalyzerService.canBeCreatedWithNoArgsAndFields(context.getObject(), context.getObjectState())) {
            return null;
        }

        ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName());

        StringBuilder fieldsInitCode = new StringBuilder();
        List<ObjectInfo> fieldObjectInfos = new ArrayList<>();
        for (Map.Entry<String, FieldValue> entry : context.getObjectState().entrySet()) {
            ObjectInfo fieldObjectInfo = objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), entry.getValue().getValue());
            fieldObjectInfos.add(fieldObjectInfo);
            fieldsInitCode.append(new StringGenerator()
                    .setTemplate("{{objectName}}.{{fieldName}} = {{fieldInlineCode}};\n")
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("fieldName", entry.getKey())
                    .addAttribute("fieldInlineCode", fieldObjectInfo.getInlineCode())
                    .generate());
        }

        objectInfo.initCode = new StringGenerator()
                .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}();\n" +
                        "{{fieldsInitCode}}")
                .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                .addAttribute("objectName", context.getObjectName())
                .addAttribute("fieldsInitCode", fieldsInitCode.toString())
                .generate();

        objectInfo.initDependencies = fieldObjectInfos.stream()
                .distinct()
                .collect(Collectors.toList());

        objectInfo.requiredImports.add(context.getObject().getClass().getName());
        return objectInfo;
    }
}
