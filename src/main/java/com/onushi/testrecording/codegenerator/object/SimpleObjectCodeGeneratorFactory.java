package com.onushi.testrecording.codegenerator.object;

public class SimpleObjectCodeGeneratorFactory {

    ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName, String inlineCode, String declareClassName) {
        return new ObjectCodeGenerator(object, objectName, inlineCode, declareClassName);
    }
}
