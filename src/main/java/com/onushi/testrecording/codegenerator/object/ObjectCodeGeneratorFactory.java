package com.onushi.testrecording.codegenerator.object;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ObjectCodeGeneratorFactory {
    abstract ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context);

    protected String getElementsClassName(List<ObjectCodeGenerator> objectCodeGenerators) {
        List<String> distinct = objectCodeGenerators.stream()
                .filter(x -> !x.inlineCode.equals("null"))
                .map(ObjectCodeGenerator::getActualClassName)
                .distinct()
                .collect(Collectors.toList());
        if (distinct.size() == 1) {
            return distinct.get(0);
        } else {
            return "Object";
        }
    }
}
