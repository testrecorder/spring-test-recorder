package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.test.ObjectNameGenerator;
import org.springframework.stereotype.Service;

// TODO IB !!!! !!!! !!!! this should not extend ObjectCodeGenerator but be a factory
@Service
public class SimpleObjectCodeGeneratorFactory {

    ObjectCodeGenerator createObjectCodeGenerator(Object object, String objectName, String inlineCode) {
        return new ObjectCodeGenerator(object, objectName, true, inlineCode);
    }
}
