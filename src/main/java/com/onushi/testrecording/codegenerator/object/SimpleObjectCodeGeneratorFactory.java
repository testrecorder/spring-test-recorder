package com.onushi.testrecording.codegenerator.object;

import org.springframework.stereotype.Service;

@Service
public class SimpleObjectCodeGeneratorFactory {

    ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName, String inlineCode) {
        return new ObjectCodeGenerator(object, objectName, true, inlineCode);
    }
}
