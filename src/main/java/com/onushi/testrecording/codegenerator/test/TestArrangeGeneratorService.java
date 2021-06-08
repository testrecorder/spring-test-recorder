package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.template.StringService;
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

    // TODO IB !!!! apply CodeNodes
    public String getArrangeCode(TestGenerator testGenerator) {
        List<ObjectInfo> objectsToInit = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToInit.add(testGenerator.targetObjectInfo);

        return new StringGenerator()
            .setTemplate(
                    "        // Arrange\n" +
                    "{{requiredHelperObjects}}" +
                    "{{objectsInit}}\n"
            )
            .addAttribute("requiredHelperObjects", this.testHelperObjectsGeneratorService.getRequiredHelperObjects(testGenerator).stream()
                    .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n")
                    .collect(Collectors.joining("")))
            .addAttribute("objectsInit", testObjectsInitGeneratorService.getObjectsInit(objectsToInit))
            .generate();
    }
}
