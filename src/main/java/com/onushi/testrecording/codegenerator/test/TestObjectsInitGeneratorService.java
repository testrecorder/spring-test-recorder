package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.codetree.CodeBlock;
import com.onushi.testrecording.codegenerator.codetree.CodeNode;
import com.onushi.testrecording.codegenerator.codetree.CodeStatement;
import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.template.StringService;
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
