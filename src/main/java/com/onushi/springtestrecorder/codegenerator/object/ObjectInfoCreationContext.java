package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.analyzer.object.FieldValue;
import com.onushi.springtestrecorder.codegenerator.test.TestGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class ObjectInfoCreationContext {
    private Object object;
    private String objectName;
    private boolean isObjectInSamePackageWithTest;
    private TestGenerator testGenerator;
    private Map<String, FieldValue> objectState;
}