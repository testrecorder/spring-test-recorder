package com.onushi.testrecorder.codegenerator.template;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class StringService {
    public String addPrefixOnAllLines(String input, String prefix) {
        return Arrays.stream(input.split("\n"))
                .map(x -> prefix + x)
                .collect(Collectors.joining("\n"));
    }


    public String getVariableName(String input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException("input");
        }
        int firstLowerCaseLetter = -1;
        int i = 0;
        while(i < input.length()) {
            char ch = input.charAt(i);
            if (Character.isLowerCase(ch)) {
                firstLowerCaseLetter = i;
                break;
            }
            i++;
        }

        if (firstLowerCaseLetter == -1) {
            return input.toLowerCase(Locale.ROOT);
        } else if (firstLowerCaseLetter == 0) {
            return input;
        } else if (firstLowerCaseLetter == 1) {
            return input.substring(0, firstLowerCaseLetter).toLowerCase(Locale.ROOT) + input.substring(firstLowerCaseLetter);
        }
        return input.substring(0, firstLowerCaseLetter - 1).toLowerCase(Locale.ROOT) + input.substring(firstLowerCaseLetter - 1);
    }

    public String upperCaseFirstLetter(String input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException("input");
        }
        return input.substring(0,1).toUpperCase(Locale.ROOT) + input.substring(1);
    }

    public String escape(String s){
        if (s == null) {
            return null;
        }
        return s.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\"", "\\\"");
    }
}
