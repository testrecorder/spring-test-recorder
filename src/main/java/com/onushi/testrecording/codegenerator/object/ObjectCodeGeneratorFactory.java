package com.onushi.testrecording.codegenerator.object;

public interface ObjectCodeGeneratorFactory {
    // TODO IB I could make this abstract and add some helper functions like getting list of dependencies
    ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context);
}
