/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.springtestrecorder.analyzer.object.FieldValue;
import com.onushi.springtestrecorder.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.springtestrecorder.analyzer.object.SetterInfo;
import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectInfoFactoryWithNoArgsAndSettersImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;
    private final ClassInfoService classInfoService;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectInfoFactoryWithNoArgsAndSettersImpl(ObjectInfoFactoryManager objectInfoFactoryManager,
                                                     ClassInfoService classInfoService,
                                                     ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.classInfoService = classInfoService;
        this.objectInfoFactoryManager = objectInfoFactoryManager;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (!classInfoService.hasAccessibleNoArgsConstructor(context.getObject().getClass(), context.isObjectInSamePackageWithTest())) {
            return null;
        }
        Map<String, FieldValue> objectState = context.getObjectState();
        Map<String, SetterInfo> settersForFields = objectCreationAnalyzerService.getSettersForFields(
                context.getObject(), objectState, context.isObjectInSamePackageWithTest());
        if (objectState.values().size() != settersForFields.values().size()) {
            return null;
        }

        ObjectInfo objectInfo = new ObjectInfo(context, context.getObjectName());

        StringBuilder builderSetters = new StringBuilder();
        StringBuilder simpleSetters = new StringBuilder();
        List<ObjectInfo> fieldObjectInfos = new ArrayList<>();
        List<FieldValue> sortedFields = objectState.values().stream()
                .sorted(Comparator.comparing(f -> f.getField().getName()))
                .collect(Collectors.toList());
        for (FieldValue field : sortedFields) {
            ObjectInfo fieldObjectInfo =
                    objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), field.getValue());
            fieldObjectInfos.add(fieldObjectInfo);
            SetterInfo setterInfo = settersForFields.get(field.getField().getName());
            if (setterInfo.isForBuilder()) {
                builderSetters.append(new StringGenerator()
                        .setTemplate("\n    .{{setterName}}({{fieldInlineCode}})")
                        .addAttribute("setterName", setterInfo.getName())
                        .addAttribute("fieldInlineCode", fieldObjectInfo.getInlineCode())
                        .generate());
            } else {
                simpleSetters.append(new StringGenerator()
                        .setTemplate("{{objectName}}.{{setterName}}({{fieldInlineCode}});\n")
                        .addAttribute("objectName", context.getObjectName())
                        .addAttribute("setterName", setterInfo.getName())
                        .addAttribute("fieldInlineCode", fieldObjectInfo.getInlineCode())
                        .generate());
            }
        }

        objectInfo.initCode = new StringGenerator()
                .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}()" +
                        "{{builderSetters}};\n" +
                        "{{simpleSetters}}\n")
                .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                .addAttribute("objectName", context.getObjectName())
                .addAttribute("builderSetters", builderSetters.toString())
                .addAttribute("simpleSetters", simpleSetters.toString())
                .generate();

        objectInfo.initDependencies = fieldObjectInfos.stream()
                .distinct()
                .collect(Collectors.toList());

        objectInfo.declareRequiredImports.add(context.getObject().getClass().getName());

        setVisiblePropertiesForUnknown(objectInfo, context, objectInfoFactoryManager, classInfoService);

        return objectInfo;
    }
}
