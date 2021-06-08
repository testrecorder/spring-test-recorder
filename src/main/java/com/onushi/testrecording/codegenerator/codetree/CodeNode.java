package com.onushi.testrecording.codegenerator.codetree;

public abstract class CodeNode {
    abstract String getCode();
    abstract int getRawLinesCount();

    protected int countStringLines(String str) {
        if (str == null) {
            return 0;
        }
        return str.split("\r\n|\r|\n").length;
    }
}
