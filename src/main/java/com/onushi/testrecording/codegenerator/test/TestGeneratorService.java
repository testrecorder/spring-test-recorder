package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TestGeneratorService {
    private final StringService stringService;
    public TestGeneratorService(StringService stringService) {
        this.stringService = stringService;
    }

    public String generateTestCode(TestGenerator testGenerator) {
        return getBeginMarkerString() +
                getPackageString(testGenerator) +
                getImportsString(testGenerator) +
                "\n" +
                getClassAndTestString(testGenerator) +
                getEndMarkerString();
    }

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(TestGenerator testGenerator) {
        return String.format("package %s;%n%n", testGenerator.getPackageName());
    }

    private String getImportsString(TestGenerator testGenerator) {
        return testGenerator.getRequiredImports().stream()
                .map(x -> String.format("import %s;%n", x))
                .collect(Collectors.joining(""));
    }

    private String getClassAndTestString(TestGenerator testGenerator) {
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
                "        {{resultType}} result = {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}});\n\n" +

                "        // Assert\n" +
                        "{{expectedResultInit}}" +
                "        assertEquals({{expectedResult}}, result);\n" +
                "    }\n" +
                "}\n");
        stringGenerator.addAttribute("testClassName", testGenerator.getShortClassName() + "Test");
        stringGenerator.addAttribute("methodName", testGenerator.getMethodName());
        stringGenerator.addAttribute("requiredHelperObjects", testGenerator.getRequiredHelperObjects().stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));
        stringGenerator.addAttribute("objectsInit", testGenerator.getObjectsInit().stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));
        stringGenerator.addAttribute("className", testGenerator.getShortClassName());
        stringGenerator.addAttribute("targetObjectName", testGenerator.getTargetObjectCodeGenerator().getObjectName());

        // TODO IB result can be asserted like this only when equals exists
        // TODO IB if return is void we don't assert the result, but we assert the changes on the target and arguments
        if (testGenerator.getResultType().isPrimitive()) {
            stringGenerator.addAttribute("resultType", testGenerator.getResultType().getCanonicalName());
        } else {
            stringGenerator.addAttribute("resultType", testGenerator.getResultType().getSimpleName());
        }

        stringGenerator.addAttribute("argumentsInlineCode", String.join(", ", testGenerator.getArgumentsInlineCode()));
        stringGenerator.addAttribute("expectedResultInit", "");
        if (!testGenerator.getResultInit().equals("")) {
            stringGenerator.addAttribute("expectedResultInit",
                    stringService.addPrefixOnAllLines(testGenerator.getResultInit(), "        ") + "\n");
        }
        stringGenerator.addAttribute("expectedResult", testGenerator.getResultObjectCodeGenerator().getInlineCode());

        return stringGenerator.generate();
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
