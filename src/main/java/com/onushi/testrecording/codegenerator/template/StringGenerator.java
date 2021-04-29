package com.onushi.testrecording.codegenerator.template;

import java.util.HashMap;
import java.util.Map;

public class StringGenerator {
    private String template;
    private final Map<String, String> context = new HashMap<>();

    public StringGenerator() {
    }

    public StringGenerator setTemplate(String template) {
        this.template = template;
        return this;
    }

    public StringGenerator addAttribute(String name, String value) {
        context.put(name, value);
        return this;
    }

    public StringGenerator addAttributes(Map<String, String> attributes) {
        context.putAll(attributes);
        return this;
    }


    public String generate() {
        String result = template;
        for (String key: context.keySet()) {
            String value = context.get(key);
            result = result.replace("{{" + key + "}}", value);
        }
        return result;
    }
}
