package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;

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
                    .setTemplate("{{composedClassNameForDeclare}} {{objectName}} = Arrays.asList({{elementsInlineCode}});")
                    .addAttribute("composedClassNameForDeclare", objectInfo.composedClassNameForDeclare)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("elementsInlineCode", elementsInlineCode)
                    .generate();

            // TODO IB obsolete
            objectInfo.addVisibleProperty(".size()", VisibleProperty.builder()
                    .finalValue(PropertyValue.fromString(String.valueOf(elements.size())))
                    .build());
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
                // TODO IB obsolete
                objectInfo.addVisibleProperty(key, VisibleProperty.builder()
                        .finalValue(PropertyValue.fromObjectInfo(element))
                        .build());
                addVisiblePropertySnapshot(objectInfo, key, context.getTestGenerator().getCurrentTestRecordingPhase(),
                        VisiblePropertySnapshot.builder()
                                .value(PropertyValue.fromObjectInfo(element))
                                .build());
            }

            return objectInfo;
        } else {
            return null;
        }
    }
}
