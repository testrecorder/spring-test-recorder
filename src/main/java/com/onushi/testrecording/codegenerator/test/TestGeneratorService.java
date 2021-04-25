package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class TestGeneratorService {
    public String generateTestCode(TestGenenerator testGenenerator) {
        return getBeginMarkerString() +
                getPackageString(testGenenerator) +
                getImportsString(testGenenerator) +
                "\n" +
                getClassAndTestString(testGenenerator) +
                getEndMarkerString();
    }

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(TestGenenerator testGenenerator) {
        return String.format("package %s;%n%n", testGenenerator.getPackageName());
    }

    private String getImportsString(TestGenenerator testGenenerator) {
        return testGenenerator.getRequiredImports().stream()
                .map(x -> String.format("import %s;%n", x))
                .collect(Collectors.joining(""));
    }

    private String getClassAndTestString(TestGenenerator testGenenerator) {
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
        stringGenerator.addAttribute("testClassName", testGenenerator.getShortClassName() + "Test");
        stringGenerator.addAttribute("methodName", testGenenerator.getMethodName());
        stringGenerator.addAttribute("requiredHelperObjects", testGenenerator.getRequiredHelperObjects().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining("")));
        stringGenerator.addAttribute("objectsInit", testGenenerator.getObjectsInit().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining("")));
        stringGenerator.addAttribute("className", testGenenerator.getShortClassName());
        stringGenerator.addAttribute("targetObjectName", testGenenerator.getTargetObjectCodeGenerator().getObjectName());

        // TODO IB result can be asserted like this only when equals exists
        // TODO IB if return is void we don't assert the result, but we assert the changes on the target and arguments
        if (testGenenerator.getResultType().isPrimitive()) {
            stringGenerator.addAttribute("resultType", testGenenerator.getResultType().getCanonicalName());
        } else {
            stringGenerator.addAttribute("resultType", testGenenerator.getResultType().getSimpleName());
        }

        stringGenerator.addAttribute("argumentsInlineCode", String.join(", ", testGenenerator.getArgumentsInlineCode()));
        stringGenerator.addAttribute("expectedResultInit", "");
        if (!testGenenerator.getResultInit().equals("")) {
            stringGenerator.addAttribute("expectedResultInit", String.format("        %s%n", testGenenerator.getResultInit()));
        }
        stringGenerator.addAttribute("expectedResult", testGenenerator.getResultObjectCodeGenerator().getInlineCode());

        return stringGenerator.generate();
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
