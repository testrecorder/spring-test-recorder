package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.test.TestGenerator;
import lombok.Getter;
import lombok.Setter;



public class ObjectCodeGeneratorCreationContext {
    @Getter @Setter private Object object;
    @Getter @Setter private String objectName;
    @Getter @Setter private TestGenerator testGenerator;
}
