package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.generator.template.StringGenerator;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

// TODO IB use a templating engine here. see https://www.baeldung.com/thymeleaf-generate-pdf
@Service
public class TestGenService {
    private final ClassInfoService classInfoService;

    public TestGenService(ClassInfoService classInfoService) {
        this.classInfoService = classInfoService;
    }

    public String generateTestString(TestGenInfo testGenInfo) {
        return getBeginMarkerString() +
                getPackageString(testGenInfo) +
                getImportsString(testGenInfo) +
                "\n" +
                getClassAndTestString(testGenInfo) +
                getEndMarkerString();
    }

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(TestGenInfo testGenInfo) {
        return String.format("package %s;%n%n", testGenInfo.getPackageName());
    }

    private String getImportsString(TestGenInfo testGenInfo) {
        return testGenInfo.getRequiredImports().stream()
                .map(x -> String.format("import %s;%n", x))
                .collect(Collectors.joining(""));
    }

    private String getClassAndTestString(TestGenInfo testGenInfo) {
        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate(
                "class {{testClassName}} {\n" +
                "    @Test\n" +
                "    void {{methodName}}() throws Exception {\n" +
                "        // Arrange\n" +
                        "{{requiredHelperObjects}}" +
                        "{{objectsInit}}" +
                "        {{className}} {{targetObjectName}} = new {{className}}();\n\n"+

                "        // Act\n" +
                "        {{resultClassName}} result = {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}});\n\n" +

                "        // Assert\n" +
                        "{{resultInit}}" +
                "        assertEquals(result, {{expectedResult}});\n" +
                "    }\n" +
                "}\n");
        stringGenerator.addAttribute("testClassName", testGenInfo.getShortClassName() + "Test");
        stringGenerator.addAttribute("methodName", testGenInfo.getMethodName());
        stringGenerator.addAttribute("requiredHelperObjects", testGenInfo.getRequiredHelperObjects().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining("")));
        stringGenerator.addAttribute("objectsInit", testGenInfo.getObjectsInit().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining("")));
        stringGenerator.addAttribute("className", testGenInfo.getShortClassName());
        stringGenerator.addAttribute("targetObjectName", testGenInfo.getTargetObjectInfo().getObjectName());
        stringGenerator.addAttribute("resultClassName", classInfoService.getShortClassName(testGenInfo.getResultObjectInfo().getObject()));
        stringGenerator.addAttribute("argumentsInlineCode", String.join(", ", testGenInfo.getArgumentsInlineCode()));
        stringGenerator.addAttribute("resultInit", "");
        if (!testGenInfo.getResultInit().equals("")) {
            stringGenerator.addAttribute("resultInit", String.format("        %s%n", testGenInfo.getResultInit()));
        }
        stringGenerator.addAttribute("expectedResult", testGenInfo.getResultObjectInfo().getInlineCode());

        return stringGenerator.generate();
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
