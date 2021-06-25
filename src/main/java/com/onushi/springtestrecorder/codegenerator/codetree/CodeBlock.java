/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.codetree;

import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CodeBlock extends CodeNode {
    private final List<CodeNode> prerequisite = new ArrayList<>();
    private final List<CodeNode> children = new ArrayList<>();
    static final int CHILD_RAW_LINES_COUNT_FOR_SPLIT_BLOCK = 6;
    static final int CHILDREN_COUNT_FOR_SPLIT_BLOCK = 5;
    static final int TOTAL_RAW_LINES_COUNT_FOR_SPLIT_BLOCK = 7;

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
        boolean splitBlock = shouldSplitBlock();

        List<String> codeList = getGetCodeForNonEmptyChildren();

        for (int i = 0; i < codeList.size(); i++) {
            String code = codeList.get(i);
            if (splitBlock && i != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(code);
        }
        return stringBuilder.toString();
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

    @Override
    public String toString() {
        List<CodeNode> allChildren = getAllChildren();
        return new StringGenerator()
                .setTemplate("CodeBlock {" +
                        "rawLinesOfCode = {{rawLinesOfCode}}, " +
                        "childrenCount = {{childrenCount}}, " +
                        "shouldSplitBlock = {{shouldSplitBlock}}}")
                .addAttribute("rawLinesOfCode", getRawLinesCount())
                .addAttribute("childrenCount", allChildren.size())
                .addAttribute("shouldSplitBlock", String.valueOf(shouldSplitBlock()))
                .generate();
    }

    private boolean shouldSplitBlock() {
        List<CodeNode> allChildren = getAllChildren();
        List<String> codeForNonEmptyChildren = getGetCodeForNonEmptyChildren();
        if (codeForNonEmptyChildren.size() >= CHILDREN_COUNT_FOR_SPLIT_BLOCK) {
            return true;
        } else if (getRawLinesCount() >= TOTAL_RAW_LINES_COUNT_FOR_SPLIT_BLOCK) {
            return true;
        } else {
            int maxChildLinesCount = 0;
            for (CodeNode codeNode : allChildren) {
                int childLinesCount = codeNode.getRawLinesCount();
                if (childLinesCount > maxChildLinesCount) {
                    maxChildLinesCount = childLinesCount;
                }
            }
            return maxChildLinesCount >= CHILD_RAW_LINES_COUNT_FOR_SPLIT_BLOCK;
        }
    }

    private List<CodeNode> getAllChildren() {
        List<CodeNode> allChildren = new ArrayList<>(prerequisite);
        allChildren.addAll(children);
        return allChildren;
    }

    private List<String> getGetCodeForNonEmptyChildren() {
        List<String> codeList = getAllChildren().stream()
                .map(CodeNode::getCode)
                .collect(Collectors.toList());

        return codeList.stream().filter(x -> !x.equals(""))
                .collect(Collectors.toList());
    }
}
