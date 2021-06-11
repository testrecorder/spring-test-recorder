package com.onushi.testrecorder.codegenerator.object;

public class ObjectInfoFactoryForCyclicDependencyImpl extends ObjectInfoFactory{
    @Override
    ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), "...");
        objectInfo.initCode = "// TODO Solve initialisation for cyclic dependency in " + context.getObject().getClass().getName() + "\n";
        return objectInfo;
    }
}
