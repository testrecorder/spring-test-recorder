package com.onushi.testrecording.codegenerator.codetree;

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
