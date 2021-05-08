package com.onushi.testrecording.codegenerator.template;

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


    public String lowerCaseFirstLetter(String input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException("input");
        }
        return input.substring(0,1).toLowerCase(Locale.ROOT) + input.substring(1);
    }

    public String upperCaseFirstLetter(String input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException("input");
        }
        return input.substring(0,1).toUpperCase(Locale.ROOT) + input.substring(1);
    }
}
