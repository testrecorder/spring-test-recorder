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

            objectInfo.initRequiredImports = Arrays.asList("java.util.Map", "java.util.HashMap");

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

            List<ObjectInfo> keyGenerators = keys.stream()
                    .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            List<ObjectInfo> valueGenerators = values.stream()
                    .distinct()
                    .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            List<ObjectInfo> allDependencies = new ArrayList<>(keyGenerators);
            allDependencies.addAll(valueGenerators);

            objectInfo.initDependencies = allDependencies;

            String keyClassName = getElementsClassName(keyGenerators);
            String valueClassName = getElementsClassName(valueGenerators);

            String elementsInlineCode = keys.stream()
                    .map(key ->  new StringGenerator()
                            .setTemplate("{{objectName}}.put({{key}}, {{value}});\n")
                            .addAttribute("objectName", context.getObjectName())
                            .addAttribute("key", objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), key).getInlineCode())
                            .addAttribute("value", objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), hashMap.get(key)).getInlineCode())
                            .generate())
                    .collect(Collectors.joining(""));

            objectInfo.initCode = new StringGenerator()
                    .setTemplate("Map<{{keyClassName}}, {{valueClassName}}> {{objectName}} = new HashMap<>();\n" +
                            "{{elementsInlineCode}}")
                    .addAttribute("keyClassName", keyClassName)
                    .addAttribute("valueClassName", valueClassName)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("elementsInlineCode", elementsInlineCode)
                    .generate();

            objectInfo.actualClassName = new StringGenerator()
                    .setTemplate("Map<{{keyClassName}}, {{valueClassName}}>")
                    .addAttribute("keyClassName", keyClassName)
                    .addAttribute("valueClassName", valueClassName)
                    .generate();

            return objectInfo;
        } else {
            return null;
        }
    }
}
