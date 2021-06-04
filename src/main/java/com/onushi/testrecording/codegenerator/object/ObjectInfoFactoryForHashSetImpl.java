package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

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

            objectInfo.requiredImports = Arrays.asList("java.util.Set", "java.util.HashSet");

            HashSet<Object> hashSet = (HashSet<Object>)context.getObject();

            List<Object> objects = Arrays.stream(hashSet.toArray())
                    .sorted(Comparator.comparing(k -> {
                        if (k == null) {
                            return "";
                        } else {
                            return k.toString();
                        }
                    }))
                    .collect(Collectors.toList());

            objectInfo.initDependencies = objects.stream()
                    .map(element1 -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element1))
                    .collect(Collectors.toList());

            String elementClassName = getElementsClassName(objectInfo.initDependencies);

            String elementsInlineCode = objects.stream()
                    .map(element ->  new StringGenerator()
                            .setTemplate("{{objectName}}.add({{element}});\n")
                            .addAttribute("objectName", context.getObjectName())
                            .addAttribute("element", objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element).getInlineCode())
                            .generate())
                    .collect(Collectors.joining(""));


            objectInfo.initCode = new StringGenerator()
                    .setTemplate("Set<{{elementClassName}}> {{objectName}} = new HashSet<>();\n" +
                            "{{elementsInlineCode}}")
                    .addAttribute("elementClassName", elementClassName)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("elementsInlineCode", elementsInlineCode)
                    .generate();

            objectInfo.actualClassName = new StringGenerator()
                    .setTemplate("Set<{{elementClassName}}>")
                    .addAttribute("elementClassName", elementClassName)
                    .generate();

            return objectInfo;
        } else {
            return null;
        }
    }
}
