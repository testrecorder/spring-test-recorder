package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import com.onushi.testrecording.codegenerator.test.TestObjectsManagerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArrayListCodeGeneratorFactory {
    private final TestObjectsManagerService testObjectsManagerService;

    public ArrayListCodeGeneratorFactory(TestObjectsManagerService testObjectsManagerService) {
        this.testObjectsManagerService = testObjectsManagerService;
    }

    public ObjectCodeGenerator createObjectCodeGenerator(TestGenerator testGenerator, Object object, String objectName) {
        ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(object, objectName, false, objectName);

        objectCodeGenerator.requiredImports = Arrays.asList("java.util.ArrayList;", "java.util.List", "java.util.Arrays");

        objectCodeGenerator.dependencies = ((List<Object>) object).stream()
                .map(element -> testObjectsManagerService.getCommonObjectCodeGenerator(testGenerator, element))
                .collect(Collectors.toList());

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
}
