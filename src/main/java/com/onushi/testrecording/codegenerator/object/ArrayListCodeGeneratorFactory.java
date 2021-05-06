package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArrayListCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public ArrayListCodeGeneratorFactory(ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

        objectCodeGenerator.requiredImports = Arrays.asList("java.util.List", "java.util.Arrays");

        // TODO IB this part is repeated
        List<ObjectCodeGenerator> elements = ((List<Object>) context.getObject()).stream()
                .map(element -> objectCodeGeneratorFactory.getCommonObjectCodeGenerator(context.getTestGenerator(), element))
                .collect(Collectors.toList());

        objectCodeGenerator.dependencies = elements.stream()
                .distinct()
                .collect(Collectors.toList());

        String elementClassSimpleName = getElementClassSimpleName((List<Object>) context.getObject());

        String elementsInlineCode = elements.stream()
                .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));

        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate("List<{{elementClassSimpleName}}> {{objectName}} = Arrays.asList({{elementsInlineCode}});")
                .addAttribute("elementClassSimpleName", elementClassSimpleName)
                .addAttribute("objectName", context.getObjectName())
                .addAttribute("elementsInlineCode", elementsInlineCode);

        objectCodeGenerator.initCode = stringGenerator.generate();

        objectCodeGenerator.declareClassName =  new StringGenerator()
                .setTemplate("List<{{elementClassSimpleName}}>")
                .addAttribute("elementClassSimpleName", elementClassSimpleName)
                .generate();

        return objectCodeGenerator;
    }

    private String getElementClassSimpleName(List<Object> list) {
        List<String> elementsClassSimpleNames = list.stream()
                .filter(Objects::nonNull)
                .map(x -> x.getClass().getSimpleName())
                .distinct()
                .collect(Collectors.toList());
        if (elementsClassSimpleNames.size() == 1) {
            return elementsClassSimpleNames.get(0);
        } else {
            return "Object";
        }
    }
}
