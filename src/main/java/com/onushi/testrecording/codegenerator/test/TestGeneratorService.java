package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGenerator;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestGeneratorService {
    private final StringService stringService;
    private final ClassInfoService classInfoService;
    public TestGeneratorService(StringService stringService, ClassInfoService classInfoService) {
        this.stringService = stringService;
        this.classInfoService = classInfoService;
    }

    public final String COMMENT_BEFORE_TEST =
            "    //TODO rename the test to describe the use case\n" +
            "    //TODO refactor the generated code to make it easier to understand\n";

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
                getTestGeneratedDateTime() +
                COMMENT_BEFORE_TEST +
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

    private String getTestGeneratedDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return new StringGenerator()
                .setTemplate("    //Test Generated on {{datetime}} with @RecordTest\n")
                .addAttribute("datetime", simpleDateFormat.format(new Date()))
                .generate();
    }

    private Map<String, String> getStringGeneratorAttributes(TestGenerator testGenerator) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("testClassName", testGenerator.getShortClassName() + "Test");
        attributes.put("methodName", testGenerator.getMethodName());
        attributes.put("requiredHelperObjects", testGenerator.getRequiredHelperObjects().stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));
        attributes.put("objectsInit", testGenerator.getObjectsInit().stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));
        // attributes.put("targetObjectInit", stringService.addPrefixOnAllLines(testGenerator.getTargetObjectCodeGenerator().getInitCode(), "        "));
        attributes.put("targetObjectName", testGenerator.getTargetObjectCodeGenerator().getObjectName());

        attributes.put("resultDeclareClassName", testGenerator.getResultDeclareClassName());

        attributes.put("argumentsInlineCode", String.join(", ", testGenerator.getArgumentsInlineCode()));
        attributes.put("expectedResultInit", testGenerator.getExpectedResultInit().stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));
        attributes.put("expectedResult", testGenerator.getExpectedResultObjectCodeGenerator().getInlineCode());
        if (testGenerator.getExpectedException() != null) {
            attributes.put("expectedExceptionClassName", testGenerator.getExpectedException().getClass().getName());
        }
        return attributes;
    }

    private String getArrangeCode(TestGenerator testGenerator, Map<String, String> attributes) {
        return new StringGenerator()
            .addAttributes(attributes)
            .setTemplate(
                "        // Arrange\n" +
                        "{{requiredHelperObjects}}" +
                        "{{objectsInit}}\n"
            ).generate();
    }

    private String getActCode(TestGenerator testGenerator, Map<String, String> attributes) {
        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.addAttributes(attributes);
        if (testGenerator.getExpectedException() == null) {
            if (testGenerator.getResultDeclareClassName().equals("void")) {
                stringGenerator.setTemplate(
                    "        // Act\n" +
                    "        {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}});\n\n");
            } else {
                stringGenerator.setTemplate(
                    "        // Act\n" +
                    "        {{resultDeclareClassName}} result = {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}});\n\n");
            }
        } else {
            stringGenerator.setTemplate(
                "        // Act & Assert\n" +
                "        assertThrows({{expectedExceptionClassName}}.class, () -> {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}}));\n");

        }

        return stringGenerator.generate();
    }

    private String getAssertCode(TestGenerator testGenerator, Map<String, String> attributes) {
        if (testGenerator.getExpectedException() != null) {
            return "";
        } else if (testGenerator.getResultDeclareClassName().equals("void")) {
            return "";
        } else {
            return "        // Assert\n" +
                getAssertCode(testGenerator, attributes, testGenerator.getExpectedResultObjectCodeGenerator(), "result");
        }
    }

    // TODO IB !!!! refactor to pattern
    private String getAssertCode(TestGenerator testGenerator, Map<String, String> attributes,
                                 ObjectCodeGenerator objectCodeGenerator, String assertPath) {
        if (objectCodeGenerator.getObject() == null) {
            return "        assertNull(result);\n";
        } else if (objectCodeGenerator.isCanUseDoubleEqualForComparison()) {
            return new StringGenerator()
                    .addAttribute("assertPath", assertPath)
                    .addAttribute("inlineCode", objectCodeGenerator.getInlineCode())
                    .setTemplate("        assertEquals({{inlineCode}}, {{assertPath}});\n")
                    .generate();
        } else if (classInfoService.hasEquals(objectCodeGenerator.getObject().getClass())) {
            // testGenerator.expectedResultInit = getObjectsInit(Collections.singletonList(testGenerator.expectedResultObjectCodeGenerator));
            // attributes.put("expectedResultInit", testGenerator.getExpectedResultInit().stream()
            //     .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));

            // TODO IB !!!! this should be recursive here so it's not ok to use expectedResultInit
            if (objectCodeGenerator.isInitDone()) {
                return new StringGenerator()
                        .addAttribute("assertPath", assertPath)
                        .addAttribute("inlineCode", objectCodeGenerator.getInlineCode())
                        .setTemplate(
                            "        assertEquals({{inlineCode}}, {{assertPath}});\n")
                        .generate();
            }
            // TODO IB !!!! fix test for List<>
            // TODO IB !!!! continue here with other known types
        } else if (objectCodeGenerator.getObject().getClass().getName().startsWith("[")) {
            return getAssertForCollection(testGenerator, attributes, objectCodeGenerator, assertPath, "[{{index}}]", ".length");
        } if (objectCodeGenerator.getObject() instanceof List<?>) {
            return getAssertForCollection(testGenerator, attributes, objectCodeGenerator, assertPath, ".get({{index}})", ".size()");
        } else {
            return new StringGenerator()
                    .addAttribute("assertPath", assertPath)
                    .setTemplate(
                        "        // TODO Add assert for {{assertPath}} object \n")
                    .generate();
        }
    }

    private String getAssertForCollection(TestGenerator testGenerator,
                                          Map<String, String> attributes,
                                          ObjectCodeGenerator objectCodeGenerator,
                                          String assertPath,
                                          String indexSyntax,
                                          String sizeSyntax) {
        List<ObjectCodeGenerator> elements = objectCodeGenerator.getElements();
        StringBuilder elementsAssert = new StringBuilder();
        for (int i = 0, elementsSize = elements.size(); i < elementsSize; i++) {
            ObjectCodeGenerator element = elements.get(i);
            String elementAssertPath =  new StringGenerator()
                    // TODO IB replace String.valueOf() with int
                    .addAttribute("index", String.valueOf(i))
                    .addAttribute("assertPath", assertPath)
                    .setTemplate("{{assertPath}}" + indexSyntax)
                    .generate();
            elementsAssert.append(getAssertCode(testGenerator, attributes, element, elementAssertPath));
        }
        return new StringGenerator()
                .addAttribute("size", String.valueOf(elements.size()))
                .addAttribute("assertPath", assertPath)
                .addAttribute("elementsAssert", elementsAssert.toString())
                .addAttribute("sizeSyntax", sizeSyntax)
                .setTemplate(
                        "        assertEquals({{size}}, {{assertPath}}" + sizeSyntax + ");\n" +
                        "{{elementsAssert}}")
                .generate();
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
