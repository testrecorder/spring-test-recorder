package com.onushi.testrecording.codegenerator.template;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class StringService {
    public String addPrefixOnAllLines(String input, String prefix) {
        return Arrays.stream(input.split("\n"))
                .map(x -> prefix + x)
                .collect(Collectors.joining("\n"));
    }
}
