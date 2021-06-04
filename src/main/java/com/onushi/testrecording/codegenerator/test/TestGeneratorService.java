package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.object.PropertyValue;
import com.onushi.testrecording.codegenerator.object.VisibleProperty;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestGeneratorService {
    private final StringService stringService;
    public TestGeneratorService(StringService stringService) {
        this.stringService = stringService;
    }

    public final String COMMENT_BEFORE_TEST =
            "    //TODO rename the test to describe the use case\n" +
            "    //TODO refactor the generated code to make it easier to understand\n";

    public String generateTestCode(TestGenerator testGenerator) throws InvocationTargetException, IllegalAccessException {
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

    private String getClassAndTestString(TestGenerator testGenerator) throws InvocationTargetException, IllegalAccessException {
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

        List<ObjectInfo> objectsToInit = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToInit.add(testGenerator.targetObjectInfo);
        attributes.put("objectsInit", getObjectsInit(objectsToInit).stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));

        attributes.put("targetObjectName", testGenerator.getTargetObjectInfo().getObjectName());

        attributes.put("resultDeclareClassName", testGenerator.getResultDeclareClassName());

        attributes.put("argumentsInlineCode", String.join(", ", testGenerator.getArgumentsInlineCode()));
        attributes.put("expectedResult", testGenerator.getExpectedResultObjectInfo().getInlineCode());
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

    private String getAssertCode(TestGenerator testGenerator, Map<String, String> attributes) throws InvocationTargetException, IllegalAccessException {
        if (testGenerator.getExpectedException() != null) {
            return "";
        } else if (testGenerator.getResultDeclareClassName().equals("void")) {
            return "";
        } else {
            return "        // Assert\n" +
                getAssertCode(testGenerator.getExpectedResultObjectInfo(), "result");
        }
    }

    private String getAssertCode(ObjectInfo objectInfo, String assertPath) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, VisibleProperty> entry : objectInfo.getVisibleProperties().entrySet()) {
            VisibleProperty visibleProperty = entry.getValue();
            if (visibleProperty.getFinalValue().getString() != null &&
                    visibleProperty.getFinalValue().getString().equals("null")) {
                String assertString = new StringGenerator()
                        .setTemplate("        assertNull({{assertPath}});\n")
                        .addAttribute("assertPath", assertPath)
                        .generate();
                stringBuilder.append(assertString);
            } else {
                PropertyValue finalValue = visibleProperty.getFinalValue();
                String composedPath = assertPath + entry.getKey();
                if (finalValue.getObjectInfo() != null) {
                    String elementAssertCode = getAssertCode(finalValue.getObjectInfo(), composedPath);
                    stringBuilder.append(elementAssertCode);
                } else if (visibleProperty.getFinalValue().getString() != null) {
                    String assertString = new StringGenerator()
                            .setTemplate("        assertEquals({{expected}}, {{composedPath}});\n")
                            .addAttribute("expected", visibleProperty.getFinalValue().getString())
                            .addAttribute("composedPath", composedPath)
                            .generate();
                    stringBuilder.append(assertString);
                }
            }
        }
        return stringBuilder.toString();
    }
    // TODO IB !!!! use parts from here
//        } else if (classInfoService.hasEquals(objectInfo.getObject().getClass())) {
//            return new StringGenerator()
//                    .addAttribute("objectsInit", getObjectsInit(Collections.singletonList(objectInfo)).stream()
//                            .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")))
//                    .addAttribute("assertPath", assertPath)
//                    .addAttribute("inlineCode", objectInfo.getInlineCode())
//                    .setTemplate("{{objectsInit}}" +
//                        "        assertEquals({{inlineCode}}, {{assertPath}});\n")
//                    .generate();
//        } else {
//    }


    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }

    public List<String> getObjectsInit(List<ObjectInfo> objectInfos) {
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectInfo objectInfo : objectInfos) {
            allObjectsInit.addAll(getObjectsInit(objectInfo));
        }
        return allObjectsInit;
    }

    public List<String> getObjectsInit(ObjectInfo objectInfo) {
        if (objectInfo.isInitPrepared() || objectInfo.isInitDone()) {
            // to avoid cyclic traversal
            return new ArrayList<>();
        }
        objectInfo.setInitPrepared(true);
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectInfo dependency : objectInfo.getInitDependencies()) {
            allObjectsInit.addAll(getObjectsInit(dependency));
        }
        if (!objectInfo.getInitCode().equals("")) {
            allObjectsInit.add(objectInfo.getInitCode());
        }
        objectInfo.setInitDone(true);
        return allObjectsInit;
    }
}
