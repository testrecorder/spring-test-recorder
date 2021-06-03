package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectInfoFactoryForHashSetImpl extends ObjectInfoFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;

    public ObjectInfoFactoryForHashSetImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
    }

    @Override
    public ObjectInfo createObjectCodeGenerator(ObjectInfoCreationContext context) {
        if (context.getObject() instanceof HashSet<?>) {
            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName(), false);

            objectInfo.requiredImports = Arrays.asList("java.util.Set", "java.util.HashSet");

            HashSet<Object> hashSet = (HashSet<Object>)context.getObject();

            List<Object> elements = Arrays.stream(hashSet.toArray())
                    .sorted(Comparator.comparing(k -> {
                        if (k == null) {
                            return "";
                        } else {
                            return k.toString();
                        }
                    }))
                    .collect(Collectors.toList());

            objectInfo.elements = elements.stream()
                    .map(element -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            objectInfo.dependencies = objectInfo.elements;

            String elementClassName = getElementsClassName(objectInfo.dependencies);

            String elementsInlineCode = elements.stream()
                    .map(element ->  new StringGenerator()
                            .setTemplate("{{objectName}}.add({{element}});\n")
                            .addAttribute("objectName", context.getObjectName())
                            .addAttribute("element", objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), element).getInlineCode())
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
