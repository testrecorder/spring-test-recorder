/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.object;

public class ObjectInfoFactoryForCyclicDependencyImpl extends ObjectInfoFactory{
    @Override
    ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        ObjectInfo objectInfo = new ObjectInfo(context, "...");
        objectInfo.initCode = "// TODO Solve initialisation for cyclic dependency in " + context.getObject().getClass().getName() + "\n";
        return objectInfo;
    }
}
