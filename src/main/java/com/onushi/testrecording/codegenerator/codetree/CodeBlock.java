package com.onushi.testrecording.codegenerator.codetree;

import java.util.ArrayList;
import java.util.List;

public class CodeBlock extends CodeNode {
    private final List<CodeNode> prerequisite = new ArrayList<>();
    private final List<CodeNode> children = new ArrayList<>();
    static final int RAW_LINES_COUNT_FOR_SPLIT_BLOCK = 7;
    static final int CHILDREN_COUNT_FOR_SPLIT_BLOCK = 5;

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
        List<CodeNode> allChildren = new ArrayList<>(prerequisite);
        allChildren.addAll(children);

        boolean splitBlock = shouldSplitBlock(allChildren);

        for (int i = 0; i < allChildren.size(); i++) {
            CodeNode codeNode = allChildren.get(i);
            if (splitBlock && i != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(codeNode.getCode());
        }
        return stringBuilder.toString();
    }

    private boolean shouldSplitBlock(List<CodeNode> allChildren) {
        boolean result = false;
        int childrenCount = children.size();
        if (childrenCount >= CHILDREN_COUNT_FOR_SPLIT_BLOCK) {
            result = true;
        } else {
            int maxChildLinesCount = 0;
            for (CodeNode codeNode : allChildren) {
                int childLinesCount = codeNode.getRawLinesCount();
                if (childLinesCount > maxChildLinesCount) {
                    maxChildLinesCount = childLinesCount;
                }
            }
            if (maxChildLinesCount >= RAW_LINES_COUNT_FOR_SPLIT_BLOCK) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int getRawLinesCount() {
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
