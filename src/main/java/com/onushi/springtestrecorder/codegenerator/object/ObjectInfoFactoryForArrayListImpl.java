/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;
import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectInfoFactoryForArrayListImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;

    public ObjectInfoFactoryForArrayListImpl(ObjectInfoFactoryManager objectInfoFactoryManager) {
        this.objectInfoFactoryManager = objectInfoFactoryManager;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject() instanceof List<?>) {
            ObjectInfo objectInfo = new ObjectInfo(context, context.getObjectName());

            @SuppressWarnings("unchecked")
            List<ObjectInfo> elements = ((List<Object>) context.getObject()).stream()
                    .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            objectInfo.declareRequiredImports = new ArrayList<>();
            objectInfo.declareRequiredImports.add("java.util.List");
            objectInfo.declareRequiredImports.addAll(getElementsDeclareRequiredImports(elements));

            objectInfo.initRequiredImports = Collections.singletonList("java.util.Arrays");

            objectInfo.initDependencies = elements.stream()
                    .distinct()
                    .collect(Collectors.toList());

            String elementClassName = getElementsComposedClassNameForDeclare(elements);

            String elementsInlineCode = elements.stream()
                    .map(ObjectInfo::getInlineCode).collect(Collectors.joining(", "));

            objectInfo.composedClassNameForDeclare = new StringGenerator()
                    .setTemplate("List<{{elementClassName}}>")
                    .addAttribute("elementClassName", elementClassName)
                    .generate();

            objectInfo.initCode = new StringGenerator()
                    .setTemplate("{{composedClassNameForDeclare}} {{objectName}} = new ArrayList<>(Arrays.asList({{elementsInlineCode}}));")
                    .addAttribute("composedClassNameForDeclare", objectInfo.composedClassNameForDeclare)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("elementsInlineCode", elementsInlineCode)
                    .generate();

            takeSnapshot(objectInfo, context);

            if (context.getTestGenerator().getCurrentTestRecordingPhase() != TestRecordingPhase.AFTER_METHOD_RUN) {
                objectInfo.toRunAfterMethodRun = () -> takeSnapshot(objectInfo, context);
            }

            return objectInfo;
        } else {
            return null;
        }
    }

    void takeSnapshot(ObjectInfo objectInfo, ObjectInfoCreationContext context) {
        @SuppressWarnings("unchecked")
        List<ObjectInfo> elements = ((List<Object>) context.getObject()).stream()
                .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                .collect(Collectors.toList());

        addVisiblePropertySnapshot(objectInfo, ".size()", context.getTestGenerator().getCurrentTestRecordingPhase(),
                VisiblePropertySnapshot.builder()
                        .value(PropertyValue.fromString(String.valueOf(elements.size())))
                        .build());
        for (int i = 0; i < elements.size(); i++) {
            ObjectInfo element = elements.get(i);
            String key = new StringGenerator()
                    .setTemplate(".get({{index}})")
                    .addAttribute("index", i)
                    .generate();
            addVisiblePropertySnapshot(objectInfo, key, context.getTestGenerator().getCurrentTestRecordingPhase(),
                    VisiblePropertySnapshot.builder()
                            .value(PropertyValue.fromObjectInfo(element))
                            .build());
        }
    }
}
