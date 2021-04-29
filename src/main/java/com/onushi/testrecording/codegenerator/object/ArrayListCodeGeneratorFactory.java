package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArrayListCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public ArrayListCodeGeneratorFactory(ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    public ObjectCodeGenerator createObjectCodeGenerator(TestGenerator testGenerator, Object object, String objectName) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, objectName);

        objectCodeGenerator.requiredImports = Arrays.asList("java.util.List", "java.util.Arrays");

        // TODO IB this part is repeated
        objectCodeGenerator.dependencies = ((List<Object>) object).stream()
                .distinct()
                .map(element -> objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, element))
                .collect(Collectors.toList());

        String elementClassSimpleName = getElementClassSimpleName((List<Object>) object);

        String elementsInlineCode = objectCodeGenerator.dependencies.stream()
                .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));

        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate("List<{{elementClassSimpleName}}> {{objectName}} =  Arrays.asList({{elementsInlineCode}});\n")
                .addAttribute("elementClassSimpleName", elementClassSimpleName)
                .addAttribute("objectName", objectName)
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
