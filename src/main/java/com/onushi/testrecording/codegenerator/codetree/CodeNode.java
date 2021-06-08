package com.onushi.testrecording.codegenerator.codetree;

public abstract class CodeNode {
    public abstract String getCode();
    public abstract int getRawLinesCount();

    protected int countStringLines(String str) {
        if (str == null) {
            return 0;
        }
        return str.split("\r\n|\r|\n").length;
    }
}
