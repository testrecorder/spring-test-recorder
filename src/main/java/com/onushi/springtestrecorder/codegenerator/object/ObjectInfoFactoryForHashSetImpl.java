package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;

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
            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName());

            @SuppressWarnings("unchecked")
            HashSet<Object> hashSet = (HashSet<Object>)context.getObject();

            List<ObjectInfo> elements = Arrays.stream(hashSet.toArray())
                    .sorted(Comparator.comparing(k -> {
                        if (k == null) {
                            return "";
                        } else {
                            return k.toString();
                        }
                    }))
                    .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

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

            objectInfo.addVisibleProperty(".size()", VisibleProperty.builder()
                    .finalValue(PropertyValue.fromString(String.valueOf(elements.size())))
                    .build());

            for (ObjectInfo element : elements) {
                String key = new StringGenerator()
                        .setTemplate(".contains({{inline}})")
                        .addAttribute("inline", element.getInlineCode())
                        .generate();
                objectInfo.addVisibleProperty(key, VisibleProperty.builder()
                        .finalValue(PropertyValue.fromString("true"))
                        .finalDependencies(Collections.singletonList(element))
                        .build());
            }

            return objectInfo;
        } else {
            return null;
        }
    }
}
