package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
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
        Map<String, String> attributes = getStringGeneratorAttributes(testGenerator);
        stringGenerator.setTemplate(
                "class {{testClassName}} {\n" +
                "    @Test\n" +
                "    void {{methodName}}() throws Exception {\n" +
                        getArrangeCode(testGenerator, attributes) +
                        getActCode(testGenerator, attributes) +
                        getAssertCode(testGenerator, attributes) +
                "    }\n" +
                "}\n");
        stringGenerator.addAttributes(attributes);
        return stringGenerator.generate();
    }

    private Map<String, String> getStringGeneratorAttributes(TestGenerator testGenerator) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("testClassName", testGenerator.getShortClassName() + "Test");
        attributes.put("methodName", testGenerator.getMethodName());
        attributes.put("requiredHelperObjects", testGenerator.getRequiredHelperObjects().stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));
        attributes.put("objectsInit", testGenerator.getObjectsInit().stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));
        attributes.put("className", testGenerator.getShortClassName());
        attributes.put("targetObjectName", testGenerator.getTargetObjectCodeGenerator().getObjectName());

        // TODO IB result can be asserted like this only when equals exists
        // TODO IB if return is void we don't assert the result, but we assert the changes on the target and non-component dependencies
        if (testGenerator.getResultType().isPrimitive()) {
            attributes.put("resultType", testGenerator.getResultType().getCanonicalName());
        } else {
            attributes.put("resultType", testGenerator.getResultType().getSimpleName());
        }

        attributes.put("argumentsInlineCode", String.join(", ", testGenerator.getArgumentsInlineCode()));
        attributes.put("expectedResultInit", "");
        if (!testGenerator.getExpectedResultInit().equals("")) {
            attributes.put("expectedResultInit",
                    stringService.addPrefixOnAllLines(testGenerator.getExpectedResultInit(), "        ") + "\n");
        }
        attributes.put("expectedResult", testGenerator.getExpectedResultObjectCodeGenerator().getInlineCode());
        if (testGenerator.getExpectedException() != null) {
            attributes.put("expectedExceptionClassName", testGenerator.getExpectedException().getClass().getName());
        }
        return attributes;
    }

    private String getArrangeCode(TestGenerator testGenerator, Map<String, String> attributes) {
        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.addAttributes(attributes);
        stringGenerator.setTemplate(
            "        // Arrange\n" +
                    "{{requiredHelperObjects}}" +
                    "{{objectsInit}}" +
            "        {{className}} {{targetObjectName}} = new {{className}}();\n\n"
        );
        return stringGenerator.generate();

    }

    private String getActCode(TestGenerator testGenerator, Map<String, String> attributes) {
        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.addAttributes(attributes);
        if (testGenerator.getExpectedException() == null) {
            if (testGenerator.getResultType() == void.class) {
                stringGenerator.setTemplate(
                    "        // Act\n" +
                    "        {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}});\n\n");
            } else {
                stringGenerator.setTemplate(
                    "        // Act\n" +
                    "        {{resultType}} result = {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}});\n\n");
            }
        } else {
            stringGenerator.setTemplate(
                "        // Act & Assert\n" +
                "        assertThrows({{expectedExceptionClassName}}.class, () -> {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}}));\n");

        }

        return stringGenerator.generate();
    }

    private String getAssertCode(TestGenerator testGenerator, Map<String, String> attributes) {
        if (testGenerator.getExpectedException() == null && testGenerator.getResultType() != void.class) {
            StringGenerator stringGenerator = new StringGenerator();
            stringGenerator.addAttributes(attributes);
            stringGenerator.setTemplate(
                "        // Assert\n" +
                        "{{expectedResultInit}}" +
                "        assertEquals({{expectedResult}}, result);\n");
            return stringGenerator.generate();
        } else {
            return "";
        }
    }


    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
