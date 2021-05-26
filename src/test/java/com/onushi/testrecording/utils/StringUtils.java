package com.onushi.testrecording.utils;

public class StringUtils {
    public static String prepareForCompare(String input) {
        return input
            .trim()
            .replace("\n\r", "\n")
            .replace("\r", "")
            .replaceAll("\\s{4}//Test Generated on [0-9\\-\\s:\\.]* with @RecordTest\n", "");
    }
}
