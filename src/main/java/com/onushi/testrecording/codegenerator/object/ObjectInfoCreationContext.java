package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.object.FieldValue;
import com.onushi.testrecording.codegenerator.test.TestGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class ObjectInfoCreationContext {
    private Object object;
    private String objectName;
    private TestGenerator testGenerator;
    private Map<String, FieldValue> objectState;
}
