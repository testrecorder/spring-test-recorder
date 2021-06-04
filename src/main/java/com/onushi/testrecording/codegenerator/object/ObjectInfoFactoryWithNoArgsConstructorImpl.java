package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

public class ObjectInfoFactoryWithNoArgsConstructorImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;
    private final ClassInfoService classInfoService;
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectInfoFactoryWithNoArgsConstructorImpl(ObjectInfoFactoryManager objectInfoFactoryManager,
                                                      ClassInfoService classInfoService,
                                                      ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.objectInfoFactoryManager = objectInfoFactoryManager;
        this.classInfoService = classInfoService;
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(context.getObject(), context.getObjectState())){
            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName());
            objectInfo.initCode = new StringGenerator()
                    .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}();")
                    .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                    .addAttribute("objectName", context.getObjectName())
                    .generate();

            objectInfo.requiredImports.add(context.getObject().getClass().getName());

            // TODO IB !!!! activate after I understand all needed imports and helpers
            // setVisiblePropertiesForUnknown(objectInfo, context, objectInfoFactoryManager, classInfoService);

            return objectInfo;
        } else {
            return null;
        }
    }
}
