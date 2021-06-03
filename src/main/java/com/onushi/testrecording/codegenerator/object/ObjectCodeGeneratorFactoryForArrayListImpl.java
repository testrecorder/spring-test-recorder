package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryForArrayListImpl extends ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;

    public ObjectCodeGeneratorFactoryForArrayListImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
    }

    @Override
    public ObjectInfo createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject() instanceof List<?>) {
            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName(), false);

            objectInfo.requiredImports = Arrays.asList("java.util.List", "java.util.Arrays");

            objectInfo.elements = ((List<Object>) context.getObject()).stream()
                    .map(element -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            objectInfo.dependencies = objectInfo.elements.stream()
                    .distinct()
                    .collect(Collectors.toList());

            String elementClassName = getElementsClassName(objectInfo.elements);

            String elementsInlineCode = objectInfo.elements.stream()
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

            return objectInfo;
        } else {
            return null;
        }
    }
}
