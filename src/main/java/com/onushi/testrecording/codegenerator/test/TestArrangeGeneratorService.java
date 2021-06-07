package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.template.StringGenerator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestArrangeGeneratorService {
    public String getArrangeCode(Map<String, String> attributes) {
        return new StringGenerator()
                .addAttributes(attributes)
                .setTemplate(
                        "        // Arrange\n" +
                                "{{requiredHelperObjects}}" +
                                "{{objectsInit}}\n"
                ).generate();
    }
}
