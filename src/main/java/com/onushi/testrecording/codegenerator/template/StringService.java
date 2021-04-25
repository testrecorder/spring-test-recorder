package com.onushi.testrecording.codegenerator.template;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class StringService {
    public String addSuffixOnAllLines(String input, String suffix) {
        boolean endsWithCR = input.endsWith("\n");
        String result = Arrays.stream(input.split("\n"))
                .map(x -> suffix + x)
                .collect(Collectors.joining("\n"));
        if (endsWithCR) {
            result += "\n";
        }
        return result;
    }
}
