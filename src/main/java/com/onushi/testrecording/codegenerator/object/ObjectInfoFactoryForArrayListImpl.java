package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;
import java.util.Arrays;
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
            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName());

            objectInfo.requiredImports = Arrays.asList("java.util.List", "java.util.Arrays");

            List<ObjectInfo> elements = ((List<Object>) context.getObject()).stream()
                    .map(element -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            objectInfo.initDependencies = elements.stream()
                    .distinct()
                    .collect(Collectors.toList());

            String elementClassName = getElementsClassName(elements);

            String elementsInlineCode = elements.stream()
                    .map(ObjectInfo::getInlineCode).collect(Collectors.joining(", "));

            objectInfo.initCode = new StringGenerator()
                    .setTemplate("List<{{elementClassName}}> {{objectName}} = Arrays.asList({{elementsInlineCode}});")
                    .addAttribute("elementClassName", elementClassName)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("elementsInlineCode", elementsInlineCode)
                    .generate();

            objectInfo.actualClassName = new StringGenerator()
                    .setTemplate("List<{{elementClassName}}>")
                    .addAttribute("elementClassName", elementClassName)
                    .generate();

            objectInfo.addVisibleProperty(".size()", VisibleProperty.builder()
                    .finalValue(ObjectInfoOrString.fromString(String.valueOf(elements.size())))
                    .build());
            for (int i = 0; i < elements.size(); i++) {
                ObjectInfo element = elements.get(i);
                String key = new StringGenerator()
                        .setTemplate(".get({{index}})")
                        .addAttribute("index", i)
                        .generate();
                objectInfo.addVisibleProperty(key, VisibleProperty.builder()
                        .finalValue(ObjectInfoOrString.fromObjectInfo(element))
                        .build());
            }

            return objectInfo;
        } else {
            return null;
        }
    }
}
