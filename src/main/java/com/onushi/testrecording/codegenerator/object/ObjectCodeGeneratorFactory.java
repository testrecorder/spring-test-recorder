package com.onushi.testrecording.codegenerator.object;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ObjectCodeGeneratorFactory {
    abstract ObjectInfo createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context);

    protected String getElementsClassName(List<ObjectInfo> objectInfos) {
        List<String> distinct = objectInfos.stream()
                .filter(x -> !x.inlineCode.equals("null"))
                .map(ObjectInfo::getActualClassName)
                .distinct()
                .collect(Collectors.toList());
        if (distinct.size() == 1) {
            return distinct.get(0);
        } else {
            return "Object";
        }
    }
}
