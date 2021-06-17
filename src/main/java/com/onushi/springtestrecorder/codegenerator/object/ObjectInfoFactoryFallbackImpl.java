package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;

public class ObjectInfoFactoryFallbackImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;
    private final ClassInfoService classInfoService;

    public ObjectInfoFactoryFallbackImpl(ObjectInfoFactoryManager objectInfoFactoryManager, ClassInfoService classInfoService) {
        this.objectInfoFactoryManager = objectInfoFactoryManager;
        this.classInfoService = classInfoService;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        ObjectInfo objectInfo = new ObjectInfo(context, context.getObjectName());

        objectInfo.initCode = new StringGenerator()
                .setTemplate("// TODO Create this object (Could not create generic object)\n" +
                        "// {{shortClassName}} {{objectName}} = new {{shortClassName}}();\n")
                .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                .addAttribute("objectName", context.getObjectName())
                .generate();

        objectInfo.declareRequiredImports.add(context.getObject().getClass().getName());

        setVisiblePropertiesForUnknown(objectInfo, context, objectInfoFactoryManager, classInfoService);

        return objectInfo;
    }
}
