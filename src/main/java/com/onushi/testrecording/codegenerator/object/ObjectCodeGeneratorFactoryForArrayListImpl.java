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
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject() instanceof List<?>) {
            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName(), false);

            objectCodeGenerator.requiredImports = Arrays.asList("java.util.List", "java.util.Arrays");

            objectCodeGenerator.elements = ((List<Object>) context.getObject()).stream()
                    .map(element -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            objectCodeGenerator.dependencies = objectCodeGenerator.elements.stream()
                    .distinct()
                    .collect(Collectors.toList());

            String elementClassName = getElementsClassName(objectCodeGenerator.elements);

            String elementsInlineCode = objectCodeGenerator.elements.stream()
                    .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));

            objectCodeGenerator.initCode = new StringGenerator()
                    .setTemplate("List<{{elementClassName}}> {{objectName}} = Arrays.asList({{elementsInlineCode}});")
                    .addAttribute("elementClassName", elementClassName)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("elementsInlineCode", elementsInlineCode)
                    .generate();

            objectCodeGenerator.declareClassName = new StringGenerator()
                    .setTemplate("List<{{elementClassName}}>")
                    .addAttribute("elementClassName", elementClassName)
                    .generate();

            return objectCodeGenerator;
        } else {
            return null;
        }
    }
}
