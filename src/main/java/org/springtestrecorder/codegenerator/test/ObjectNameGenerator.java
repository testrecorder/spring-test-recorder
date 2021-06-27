/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import org.springtestrecorder.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

@Service
public class ObjectNameGenerator {
    private final StringService stringService;

    public ObjectNameGenerator(StringService stringService) {
        this.stringService = stringService;
    }

    public String getBaseObjectName(Object object) {
        if (object == null) {
            return "null";
        } else {
            String classSimpleName = object.getClass().getSimpleName();
            if (classSimpleName.contains("[")) {
                return "array";
            } else {
                return stringService.getVariableName(object.getClass().getSimpleName());
            }
        }
    }

    public String getNewObjectName(TestGenerator testGenerator, Object object) {
        String objectNameBase = getBaseObjectName(object);
        int newIndex;
        if (testGenerator.getLastIndexForObjectName().containsKey(objectNameBase)) {
            newIndex = testGenerator.getLastIndexForObjectName().get(objectNameBase) + 1;
        } else {
            newIndex = 1;
        }
        testGenerator.getLastIndexForObjectName().put(objectNameBase, newIndex);
        return objectNameBase + newIndex;
    }
}
