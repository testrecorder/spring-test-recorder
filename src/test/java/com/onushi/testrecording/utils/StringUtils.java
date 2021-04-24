package com.onushi.testrecording.utils;

public class StringUtils {
    public static String trimAndIgnoreCRDiffs(String input) {
        return input.trim().replace("\n\r", "\n").replace("\r", "");
    }
}
