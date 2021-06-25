/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;
import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectInfoFactoryForHashMapImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;

    public ObjectInfoFactoryForHashMapImpl(ObjectInfoFactoryManager objectInfoFactoryManager) {
        this.objectInfoFactoryManager = objectInfoFactoryManager;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject() instanceof HashMap<?, ?>) {
            ObjectInfo objectInfo = new ObjectInfo(context, context.getObjectName());

            @SuppressWarnings("unchecked")
            HashMap<Object, Object> hashMap = (HashMap<Object, Object>)context.getObject();

            List<Object> keys = getKeysList(hashMap);

            List<ObjectInfo> keyElements = keys.stream()
                    .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            List<Object> values = new ArrayList<>(hashMap.values());
            List<ObjectInfo> valueElements = values.stream()
                    .distinct()
                    .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            List<ObjectInfo> allDependencies = new ArrayList<>(keyElements);
            allDependencies.addAll(valueElements);

            objectInfo.declareRequiredImports = new ArrayList<>();
            objectInfo.declareRequiredImports.add("java.util.Map");
            objectInfo.declareRequiredImports.addAll(getElementsDeclareRequiredImports(keyElements));
            objectInfo.declareRequiredImports.addAll(getElementsDeclareRequiredImports(valueElements));

            objectInfo.initRequiredImports = Collections.singletonList("java.util.HashMap");

            objectInfo.initDependencies = allDependencies;

            String keyClassName = getElementsComposedClassNameForDeclare(keyElements);
            String valueClassName = getElementsComposedClassNameForDeclare(valueElements);

            String elementsInlineCode = keys.stream()
                    .map(key ->  new StringGenerator()
                            .setTemplate("{{objectName}}.put({{key}}, {{value}});\n")
                            .addAttribute("objectName", context.getObjectName())
                            .addAttribute("key", objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), key).getInlineCode())
                            .addAttribute("value", objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), hashMap.get(key)).getInlineCode())
                            .generate())
                    .collect(Collectors.joining(""));

            objectInfo.composedClassNameForDeclare = new StringGenerator()
                    .setTemplate("Map<{{keyClassName}}, {{valueClassName}}>")
                    .addAttribute("keyClassName", keyClassName)
                    .addAttribute("valueClassName", valueClassName)
                    .generate();

            objectInfo.initCode = new StringGenerator()
                    .setTemplate("{{composedClassNameForDeclare}} {{objectName}} = new HashMap<>();\n" +
                            "{{elementsInlineCode}}")
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

    private List<Object> getKeysList(HashMap<Object, Object> hashMap) {
        return hashMap.keySet()
                .stream()
                .sorted(Comparator.comparing(k -> {
                    if (k == null) {
                        return "";
                    } else {
                        return k.toString();
                    }
                }))
                .collect(Collectors.toList());
    }

    private void takeSnapshot(ObjectInfo objectInfo, ObjectInfoCreationContext context) {
        @SuppressWarnings("unchecked")
        HashMap<Object, Object> hashMap = (HashMap<Object, Object>)context.getObject();
        List<ObjectInfo> keyElements = getKeysList(hashMap).stream()
                .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                .collect(Collectors.toList());

        addVisiblePropertySnapshot(objectInfo, ".size()", context.getTestGenerator().getCurrentTestRecordingPhase(),
                VisiblePropertySnapshot.builder()
                        .value(PropertyValue.fromString(String.valueOf(keyElements.size())))
                        .build());

        for (ObjectInfo element : keyElements) {
            String key = new StringGenerator()
                    .setTemplate(".get({{inline}})")
                    .addAttribute("inline", element.getInlineCode())
                    .generate();
            ObjectInfo valueElement = objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), hashMap.get(element.getObject()));
            addVisiblePropertySnapshot(objectInfo, key, context.getTestGenerator().getCurrentTestRecordingPhase(),
                    VisiblePropertySnapshot.builder()
                            .value(PropertyValue.fromObjectInfo(valueElement))
                            .otherDependencies(Collections.singletonList(element))
                            .build());
        }
    }
}
