/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import org.springtestrecorder.codegenerator.codetree.CodeBlock;
import org.springtestrecorder.codegenerator.codetree.CodeNode;
import org.springtestrecorder.codegenerator.codetree.CodeStatement;
import org.springtestrecorder.codegenerator.object.ObjectInfo;
import org.springtestrecorder.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestObjectsInitGeneratorService {
    private final StringService stringService;

    public TestObjectsInitGeneratorService(StringService stringService) {
        this.stringService = stringService;
    }

    public CodeNode getObjectsInit(List<ObjectInfo> objectInfos) {
        CodeBlock result = new CodeBlock();
        for (ObjectInfo objectInfo : objectInfos) {
            result.addChild(getObjectsInit(objectInfo));
        }
        return result;
    }

    private CodeNode getObjectsInit(ObjectInfo objectInfo) {
        CodeBlock result = new CodeBlock();
        if (!objectInfo.isInitAdded()) {
            for (ObjectInfo dependency : objectInfo.getInitDependencies()) {
                result.addPrerequisite(getObjectsInit(dependency));
            }
            if (objectInfo.hasInitCode()) {
                String initCode = objectInfo.getInitCode();
                initCode = stringService.addPrefixOnAllLines(initCode, "        ") + "\n";
                result.addChild(new CodeStatement(initCode));
            }
            objectInfo.setInitAdded(true);
        }
        return result;
    }
}
