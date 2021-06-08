package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestActGeneratorService {
    public String getActCode(TestGenerator testGenerator, Map<String, String> attributes) {
        return new StringGenerator()
                .setTemplate(getTemplate(testGenerator))
                .addAttributes(attributes)
                .addAttribute("targetObjectName", testGenerator.getTargetObjectInfo().getObjectName())
                .addAttribute("resultDeclareClassName", testGenerator.getResultDeclareClassName())
                .addAttribute("argumentsInlineCode", testGenerator.argumentObjectInfos.stream()
                    .map(ObjectInfo::getInlineCode)
                    .collect(Collectors.joining(", ")))
                .generate();
    }

    private String getTemplate(TestGenerator testGenerator) {
        if (testGenerator.getExpectedException() == null) {
            if (testGenerator.getResultDeclareClassName().equals("void")) {
                return "        // Act\n" +
                       "        {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}});\n\n";
            }
            return "        // Act\n" +
                   "        {{resultDeclareClassName}} result = {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}});\n\n";
        }
        return "        // Act & Assert\n" +
               "        assertThrows({{expectedExceptionClassName}}.class, () -> {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}}));\n";
    }
}
