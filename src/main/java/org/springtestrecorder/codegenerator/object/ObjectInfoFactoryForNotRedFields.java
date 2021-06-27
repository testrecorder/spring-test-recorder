/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.object;

import org.springtestrecorder.codegenerator.template.StringGenerator;

public class ObjectInfoFactoryForNotRedFields extends ObjectInfoFactory {

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        ObjectInfo objectInfo = new ObjectInfo(context, context.getObjectName());

        objectInfo.initCode = new StringGenerator()
                .setTemplate("// TODO Create this object (Could not read object fields)\n" +
                        "// {{shortClassName}} {{objectName}} = new {{shortClassName}}();")
                .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                .addAttribute("objectName", context.getObjectName())
                .generate();

        objectInfo.declareRequiredImports.add(context.getObject().getClass().getName());
        return objectInfo;
    }
}
