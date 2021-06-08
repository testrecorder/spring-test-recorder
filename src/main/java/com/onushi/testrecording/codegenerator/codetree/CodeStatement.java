package com.onushi.testrecording.codegenerator.codetree;

public class CodeStatement extends CodeNode {
    private String code;

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
}
