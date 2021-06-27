/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.utils;

public class StringUtils {
    public static String prepareForCompare(String input) {
        return prepareForCompareNoTrim(input)
            .trim();
    }
    public static String prepareForCompareNoTrim(String input) {
        return input
                .replace("\n\r", "\n")
                .replace("\r", "")
                .replaceAll("\\s{4}//Test Generated on [0-9\\-\\s:.]* with @RecordTest\n", "");
    }
}
