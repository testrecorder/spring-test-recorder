package com.onushi.testrecording.codegenerator.codetree;

import java.util.ArrayList;
import java.util.List;

public class CodeBlock extends CodeNode {
    private final List<CodeNode> prerequisite = new ArrayList<>();
    private final List<CodeNode> children = new ArrayList<>();

    public CodeBlock addPrerequisite(CodeNode codeNode) {
        this.prerequisite.add(codeNode);
        return this;
    }

    public CodeBlock addChild(CodeNode codeNode) {
        this.children.add(codeNode);
        return this;
    }

    @Override
    public String getCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (CodeNode codeNode : prerequisite) {
            stringBuilder.append(codeNode.getCode());
        }
        for (CodeNode codeNode : children) {
            stringBuilder.append(codeNode.getCode());
        }
        return stringBuilder.toString();
    }

    @Override
    int getRawLinesCount() {
        int result = 0;
        for (CodeNode codeNode : prerequisite) {
            result += codeNode.getRawLinesCount();
        }
        for (CodeNode codeNode : children) {
            result += codeNode.getRawLinesCount();
        }
        return result;
    }
}
