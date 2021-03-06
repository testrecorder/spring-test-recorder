/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import org.springtestrecorder.codegenerator.object.ObjectInfo;
import org.springtestrecorder.codegenerator.template.StringGenerator;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TestActGeneratorService {
    public String getActCode(TestGenerator testGenerator) {
        StringGenerator stringGenerator = new StringGenerator()
            .setTemplate(getTemplate(testGenerator))
            .addAttribute("targetObjectName", testGenerator.getTargetObjectInfo().getObjectName())
            .addAttribute("methodName", testGenerator.getMethodName())
            .addAttribute("resultDeclareClassName", testGenerator.getResultDeclareClassName())
            .addAttribute("argumentsInlineCode", testGenerator.argumentObjectInfos.stream()
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.joining(", ")));

        if (testGenerator.getExpectedException() != null) {
            stringGenerator.addAttribute("expectedExceptionClassName", testGenerator.getExpectedException().getClass().getName());
        }

        return stringGenerator.generate();
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
