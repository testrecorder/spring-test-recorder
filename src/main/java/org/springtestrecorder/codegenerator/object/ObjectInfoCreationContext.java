/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.object;

import org.springtestrecorder.analyzer.object.FieldValue;
import org.springtestrecorder.codegenerator.test.TestGenerator;
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
