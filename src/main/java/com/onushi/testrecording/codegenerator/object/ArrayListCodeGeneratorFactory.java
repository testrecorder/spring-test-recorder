package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArrayListCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public ArrayListCodeGeneratorFactory(ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    public ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, false, objectName);

        objectCodeGenerator.requiredImports = Arrays.asList("java.util.ArrayList;", "java.util.List", "java.util.Arrays");

        objectCodeGenerator.dependencies = getObjectCodeGeneratorElements((List<Object>) object);
        String elementsInlineCode = objectCodeGenerator.dependencies.stream()
                .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));

        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate("List<{{getElementClassSimpleName}}> {{objectName}} =  Arrays.asList({{elementsInlineCode}});\n");

        stringGenerator.addAttribute("getElementClassSimpleName", getElementClassSimpleName((List<Object>) object));
        stringGenerator.addAttribute("objectName", objectName);
        stringGenerator.addAttribute("elementsInlineCode", elementsInlineCode);

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

    private List<ObjectCodeGenerator> getObjectCodeGeneratorElements(List<Object> list) {
        List<ObjectCodeGenerator> result = new ArrayList<>();
        for (Object element: list) {
            // TODO IB !!!! not ignored
            // TODO IB !!!! use stream
            ObjectCodeGenerator elementObjectCodeGenerator = objectCodeGeneratorFactory.createObjectCodeGenerator(element, "ignored");
            result.add(elementObjectCodeGenerator);
        }
        return result;
    }
}
