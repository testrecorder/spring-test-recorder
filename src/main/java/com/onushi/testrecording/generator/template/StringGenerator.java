package com.onushi.testrecording.generator.template;

import java.util.HashMap;
import java.util.Map;

public class StringGenerator {
    private String template;
    private Map<String, String> context = new HashMap<>();

    public StringGenerator() {
    }

    public StringGenerator setTemplate(String template) {
        this.template = template;
        return this;
    }

    public void addAttribute(String name, String value) {
        context.put(name, value);
    }

    // TODO IB LATER improve if slow
    public String generate() {
        String result = template;
        for (String key: context.keySet()) {
            String value = context.get(key);
            result = result.replace("{{" + key + "}}", value);
        }
        return result;
    }
}
