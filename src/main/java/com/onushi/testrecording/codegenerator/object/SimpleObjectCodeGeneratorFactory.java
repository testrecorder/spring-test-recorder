package com.onushi.testrecording.codegenerator.object;

public class SimpleObjectCodeGeneratorFactory {

    ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context, String inlineCode, String declareClassName) {
        return new ObjectCodeGenerator(context.getObject(), context.getObjectName(), inlineCode, declareClassName);
    }
}
