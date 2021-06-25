/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.codetree;

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
