package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

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
            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName());


            HashMap<Object, Object> hashMap = (HashMap<Object, Object>)context.getObject();

            List<Object> keys = hashMap.keySet()
                    .stream()
                    .sorted(Comparator.comparing(k -> {
                        if (k == null) {
                            return "";
                        } else {
                            return k.toString();
                        }
                    }))
                    .collect(Collectors.toList());
            List<Object> values = new ArrayList<>(hashMap.values());

            List<ObjectInfo> keyElements = keys.stream()
                    .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

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

            objectInfo.addVisibleProperty(".size()", VisibleProperty.builder()
                    .finalValue(PropertyValue.fromString(String.valueOf(keyElements.size())))
                    .build());

            for (ObjectInfo element : keyElements) {
                String key = new StringGenerator()
                        .setTemplate(".get({{inline}})")
                        .addAttribute("inline", element.getInlineCode())
                        .generate();
                ObjectInfo valueElement = objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), hashMap.get(element.getObject()));
                objectInfo.addVisibleProperty(key, VisibleProperty.builder()
                        .finalValue(PropertyValue.fromObjectInfo(valueElement))
                        .finalDependencies(Collections.singletonList(element))
                        .build());
            }

            return objectInfo;
        } else {
            return null;
        }
    }
}
