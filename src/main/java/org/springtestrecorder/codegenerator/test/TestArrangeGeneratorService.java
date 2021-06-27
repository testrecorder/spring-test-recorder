/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import org.springtestrecorder.codegenerator.object.ObjectInfo;
import org.springtestrecorder.codegenerator.template.StringGenerator;
import org.springtestrecorder.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestArrangeGeneratorService {
    private final TestHelperObjectsGeneratorService testHelperObjectsGeneratorService;
    private final StringService stringService;
    private final TestObjectsInitGeneratorService testObjectsInitGeneratorService;

    public TestArrangeGeneratorService(TestHelperObjectsGeneratorService testHelperObjectsGeneratorService,
                                       StringService stringService,
                                       TestObjectsInitGeneratorService testObjectsInitGeneratorService) {
        this.testHelperObjectsGeneratorService = testHelperObjectsGeneratorService;
        this.stringService = stringService;
        this.testObjectsInitGeneratorService = testObjectsInitGeneratorService;
    }

    public String getArrangeCode(TestGenerator testGenerator) {
        List<ObjectInfo> objectsToInit = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToInit.add(testGenerator.targetObjectInfo);

        String requiredHelperObjects = this.testHelperObjectsGeneratorService.getRequiredHelperObjects(testGenerator).stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n")
                .collect(Collectors.joining(""));
        if (!requiredHelperObjects.equals("")) {
            requiredHelperObjects += "\n";
        }

        return new StringGenerator()
            .setTemplate(
                    "        // Arrange\n" +
                    "{{requiredHelperObjects}}" +
                    "{{objectsInit}}\n"
            )
            .addAttribute("requiredHelperObjects", requiredHelperObjects)
            .addAttribute("objectsInit", testObjectsInitGeneratorService.getObjectsInit(objectsToInit).getCode())
            .generate();
    }
}
