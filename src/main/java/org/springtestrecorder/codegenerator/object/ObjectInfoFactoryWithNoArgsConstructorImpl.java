/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.object;

import org.springtestrecorder.analyzer.classInfo.ClassInfoService;
import org.springtestrecorder.analyzer.object.ObjectCreationAnalyzerService;
import org.springtestrecorder.codegenerator.template.StringGenerator;

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
        if (objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(
                context.getObject(), context.getObjectState(), context.isObjectInSamePackageWithTest())){
            ObjectInfo objectInfo = new ObjectInfo(context, context.getObjectName());
            objectInfo.initCode = new StringGenerator()
                    .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}();\n")
                    .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                    .addAttribute("objectName", context.getObjectName())
                    .generate();

            objectInfo.declareRequiredImports.add(context.getObject().getClass().getName());

            setVisiblePropertiesForUnknown(objectInfo, context, objectInfoFactoryManager, classInfoService);

            return objectInfo;
        } else {
            return null;
        }
    }
}
