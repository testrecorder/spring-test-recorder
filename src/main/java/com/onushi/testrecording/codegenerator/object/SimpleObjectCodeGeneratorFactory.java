package com.onushi.testrecording.codegenerator.object;

public class SimpleObjectCodeGeneratorFactory {

    ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName, String inlineCode) {
        return new ObjectCodeGenerator(object, objectName, inlineCode);
    }
}
