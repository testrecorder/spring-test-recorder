/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.codetree;

public class CodeStatement extends CodeNode {
    private final String code;

    public CodeStatement(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getRawLinesCount() {
        return countStringLines(code);
    }

    @Override
    public String toString() {
        return "CodeStatement { rawLinesOfCode = " + getRawLinesCount() + " }";
    }
}
