/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.object;

import org.springtestrecorder.analyzer.classInfo.ClassInfoService;
import org.springtestrecorder.analyzer.object.FieldValue;
import org.springtestrecorder.analyzer.object.ObjectCreationAnalyzerService;
import org.springtestrecorder.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectInfoFactoryWithNoArgsAndFieldsImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;
    private final ClassInfoService classInfoService;

    public ObjectInfoFactoryWithNoArgsAndFieldsImpl(ObjectInfoFactoryManager objectInfoFactoryManager,
                                                    ObjectCreationAnalyzerService objectCreationAnalyzerService,
                                                    ClassInfoService classInfoService) {
        this.objectInfoFactoryManager = objectInfoFactoryManager;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
        this.classInfoService = classInfoService;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (!objectCreationAnalyzerService.canBeCreatedWithNoArgsAndFields(
                context.getObject(), context.getObjectState(), context.isObjectInSamePackageWithTest() )) {
            return null;
        }

        ObjectInfo objectInfo = new ObjectInfo(context, context.getObjectName());

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

        objectInfo.declareRequiredImports.add(context.getObject().getClass().getName());

        setVisiblePropertiesForUnknown(objectInfo, context, objectInfoFactoryManager, classInfoService);

        return objectInfo;
    }
}
