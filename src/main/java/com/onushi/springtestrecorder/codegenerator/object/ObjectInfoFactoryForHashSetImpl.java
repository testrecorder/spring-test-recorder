package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;
import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectInfoFactoryForHashSetImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;

    public ObjectInfoFactoryForHashSetImpl(ObjectInfoFactoryManager objectInfoFactoryManager) {
        this.objectInfoFactoryManager = objectInfoFactoryManager;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject() instanceof HashSet<?>) {
            ObjectInfo objectInfo = new ObjectInfo(context, context.getObjectName());

            @SuppressWarnings("unchecked")
            HashSet<Object> hashSet = (HashSet<Object>)context.getObject();

            List<ObjectInfo> elements = getElementsList(context, hashSet);

            objectInfo.initDependencies = elements;

            objectInfo.declareRequiredImports = new ArrayList<>();
            objectInfo.declareRequiredImports.add("java.util.Set");
            objectInfo.declareRequiredImports.addAll(getElementsDeclareRequiredImports(elements));

            objectInfo.initRequiredImports = Collections.singletonList("java.util.HashSet");

            String elementClassName = getElementsComposedClassNameForDeclare(objectInfo.initDependencies);

            String elementsInlineCode = elements.stream()
                    .map(element ->  new StringGenerator()
                            .setTemplate("{{objectName}}.add({{element}});\n")
                            .addAttribute("objectName", context.getObjectName())
                            .addAttribute("element", element.getInlineCode())
                            .generate())
                    .collect(Collectors.joining(""));

            objectInfo.composedClassNameForDeclare = new StringGenerator()
                    .setTemplate("Set<{{elementClassName}}>")
                    .addAttribute("elementClassName", elementClassName)
                    .generate();

            objectInfo.initCode = new StringGenerator()
                    .setTemplate("{{composedClassNameForDeclare}} {{objectName}} = new HashSet<>();\n" +
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

    private List<ObjectInfo> getElementsList(ObjectInfoCreationContext context, HashSet<Object> hashSet) {

        return Arrays.stream(hashSet.toArray())
                .sorted(Comparator.comparing(k -> {
                    if (k == null) {
                        return "";
                    } else {
                        return k.toString();
                    }
                }))
                .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                .collect(Collectors.toList());
    }

    private void takeSnapshot(ObjectInfo objectInfo, ObjectInfoCreationContext context) {
        @SuppressWarnings("unchecked")
        HashSet<Object> hashSet = (HashSet<Object>)context.getObject();

        List<ObjectInfo> elements = getElementsList(context, hashSet);

        addVisiblePropertySnapshot(objectInfo, ".size()", context.getTestGenerator().getCurrentTestRecordingPhase(),
                VisiblePropertySnapshot.builder()
                        .value(PropertyValue.fromString(String.valueOf(elements.size())))
                        .build());

        for (ObjectInfo element : elements) {
            String key = new StringGenerator()
                    .setTemplate(".contains({{inline}})")
                    .addAttribute("inline", element.getInlineCode())
                    .generate();
            addVisiblePropertySnapshot(objectInfo, key, context.getTestGenerator().getCurrentTestRecordingPhase(),
                    VisiblePropertySnapshot.builder()
                            .value(PropertyValue.fromString("true"))
                            .otherDependencies(Collections.singletonList(element))
                            .build());
        }    }
}
