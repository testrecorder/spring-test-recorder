package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public ArrayListCodeGeneratorFactory(ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    public ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, false, objectName);

        objectCodeGenerator.requiredImports.add("java.util.ArrayList;");
        objectCodeGenerator.requiredImports.add("java.util.List");
        objectCodeGenerator.requiredImports.add("java.util.Arrays");

        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate("List<{{getElementClassSimpleName}}> {{objectName}} =  Arrays.asList({{elementsInlineCode}});\n");

        stringGenerator.addAttribute("getElementClassSimpleName", getElementClassSimpleName((List<Object>) object));
        stringGenerator.addAttribute("objectName", objectName);
        stringGenerator.addAttribute("elementsInlineCode", getElementsInlineCode((List<Object>) object));

        objectCodeGenerator.initCode = stringGenerator.generate();
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

    private String getElementsInlineCode(List<Object> list) {
        List<ObjectCodeGenerator> elementObjectCodeGenerators = new ArrayList<>();
        for(Object element: list) {
            ObjectCodeGenerator elementObjectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(element, "ignored");
            elementObjectCodeGenerators.add(elementObjectCodeGenerator);
        }
        String elementsInlineCode = elementObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));
        return elementsInlineCode;
    }
}
