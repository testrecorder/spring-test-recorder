package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.object.PropertyValue;
import com.onushi.testrecording.codegenerator.object.VisibleProperty;
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
        return getRequiredImports(testGenerator).stream()
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
                        getArrangeCode(attributes) +
                        getActCode(testGenerator, attributes) +
                        getAssertCode(testGenerator) +
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
        attributes.put("requiredHelperObjects", getRequiredHelperObjects(testGenerator).stream()
                        .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n")
                        .collect(Collectors.joining("")));

        List<ObjectInfo> objectsToInit = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToInit.add(testGenerator.targetObjectInfo);
        attributes.put("objectsInit", getObjectsInit(objectsToInit).stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));

        attributes.put("targetObjectName", testGenerator.getTargetObjectInfo().getObjectName());

        attributes.put("resultDeclareClassName", testGenerator.getResultDeclareClassName());

        attributes.put("argumentsInlineCode", testGenerator.argumentObjectInfos.stream()
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.joining(", ")));
        attributes.put("expectedResult", testGenerator.getExpectedResultObjectInfo().getInlineCode());
        if (testGenerator.getExpectedException() != null) {
            attributes.put("expectedExceptionClassName", testGenerator.getExpectedException().getClass().getName());
        }
        return attributes;
    }

    private String getArrangeCode(Map<String, String> attributes) {
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

    private String getAssertCode(TestGenerator testGenerator) {
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
        if (objectInfo.getObject() != null &&
                classInfoService.hasEquals(objectInfo.getObject().getClass()) &&
                !objectInfo.getInitCode().equals("") &&
                objectInfo.isInitAdded()) {
            return new StringGenerator()
                    .setTemplate("        assertEquals({{objectInfoName}}, {{assertPath}});\n")
                    .addAttribute("objectInfoName", objectInfo.getObjectName())
                    .addAttribute("assertPath", assertPath)
                    .generate();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, VisibleProperty> entry : objectInfo.getVisibleProperties().entrySet()) {
                VisibleProperty visibleProperty = entry.getValue();
                PropertyValue finalValue = visibleProperty.getFinalValue();
                String composedPath = assertPath + entry.getKey();

                String objectsInit = "";
                if (visibleProperty.getFinalDependencies() != null) {
                    objectsInit = getObjectsInit(visibleProperty.getFinalDependencies()).stream()
                            .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining(""));
                }
                if (visibleProperty.getFinalValue().getString() != null &&
                        visibleProperty.getFinalValue().getString().equals("null")) {
                    String assertString = new StringGenerator()
                            .setTemplate("{{objectsInit}}" +
                                    "        assertNull({{composedPath}});\n")
                            .addAttribute("composedPath", composedPath)
                            .addAttribute("objectsInit", objectsInit)
                            .generate();
                    stringBuilder.append(assertString);
                } else if (visibleProperty.getFinalValue().getString() != null &&
                        visibleProperty.getFinalValue().getString().equals("true")) {
                    String assertString = new StringGenerator()
                            .setTemplate("{{objectsInit}}" +
                                    "        assertTrue({{composedPath}});\n")
                            .addAttribute("objectsInit", objectsInit)
                            .addAttribute("composedPath", composedPath)
                            .generate();
                    stringBuilder.append(assertString);
                } else {
                    if (finalValue.getObjectInfo() != null) {
                        stringBuilder.append(objectsInit);
                        String elementAssertCode = getAssertCode(finalValue.getObjectInfo(), composedPath);
                        stringBuilder.append(elementAssertCode);
                    } else if (visibleProperty.getFinalValue().getString() != null) {
                        String assertString = new StringGenerator()
                                .setTemplate("{{objectsInit}}" +
                                        "        assertEquals({{expected}}, {{composedPath}});\n")
                                .addAttribute("objectsInit", objectsInit)
                                .addAttribute("expected", visibleProperty.getFinalValue().getString())
                                .addAttribute("composedPath", composedPath)
                                .generate();
                        stringBuilder.append(assertString);
                    }
                }
            }
            return stringBuilder.toString();
        }
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }

    private List<String> getRequiredImports(TestGenerator testGenerator) {
        List<String> result = new ArrayList<>();
        result.add("org.junit.jupiter.api.Test");
        result.add("static org.junit.jupiter.api.Assertions.*");

        List<ObjectInfo> objectsToInit = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToInit.add(testGenerator.targetObjectInfo);

        result.addAll(objectsToInit.stream()
                .flatMap(x -> getDeclareAndInitRequiredImports(x).stream())
                .collect(Collectors.toList()));

        result.addAll(testGenerator.getExpectedResultObjectInfo().getDeclareRequiredImports());

        result.addAll(getVisiblePropsRequiredImports(testGenerator.getExpectedResultObjectInfo()));

        result = result.stream()
                .distinct()
                .filter(x -> !x.startsWith(testGenerator.getPackageName() + "."))
                .collect(Collectors.toList());
        return result;
    }

    private List<String> getDeclareAndInitRequiredImports(ObjectInfo objectInfo) {
        List<String> result = new ArrayList<>(objectInfo.getDeclareRequiredImports());
        result.addAll(objectInfo.getInitRequiredImports());
        for (ObjectInfo initDependency : objectInfo.getInitDependencies()) {
            result.addAll(getDeclareAndInitRequiredImports(initDependency));
        }
        return result;
    }

    private List<String> getVisiblePropsRequiredImports(ObjectInfo objectInfo) {
        List<String> result = new ArrayList<>();
        for (String key : objectInfo.getVisibleProperties().keySet()) {
            VisibleProperty visibleProperty = objectInfo.getVisibleProperties().get(key);
            if (visibleProperty.getRequiredImports() != null) {
                result.addAll(visibleProperty.getRequiredImports());
            }
            if (visibleProperty.getFinalDependencies() != null) {
                for (ObjectInfo finalDependency : visibleProperty.getFinalDependencies()) {
                    result.addAll(getDeclareAndInitRequiredImports(finalDependency));
                }
            }
            if (visibleProperty.getFinalValue().getObjectInfo() != null) {
                result.addAll(getVisiblePropsRequiredImports(visibleProperty.getFinalValue().getObjectInfo()));
            }
        }
        return result;
    }

    private List<String> getRequiredHelperObjects(TestGenerator testGenerator) {
        List<String> result = new ArrayList<>();

        List<ObjectInfo> objectsToInit = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToInit.add(testGenerator.targetObjectInfo);

        result.addAll(objectsToInit.stream()
                .flatMap(x -> getInitRequiredHelperObjects(x).stream())
                .collect(Collectors.toList()));

        result.addAll(getVisiblePropsRequiredHelperObjects(testGenerator.getExpectedResultObjectInfo()));

        return result.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getInitRequiredHelperObjects(ObjectInfo objectInfo) {
        List<String> result = new ArrayList<>(objectInfo.getInitRequiredHelperObjects());
        for (ObjectInfo initDependency : objectInfo.getInitDependencies()) {
            result.addAll(getInitRequiredHelperObjects(initDependency));
        }
        return result;
    }

    private List<String> getVisiblePropsRequiredHelperObjects(ObjectInfo objectInfo) {
        List<String> result = new ArrayList<>();
        for (String key : objectInfo.getVisibleProperties().keySet()) {
            VisibleProperty visibleProperty = objectInfo.getVisibleProperties().get(key);
            if (visibleProperty.getRequiredHelperObjects() != null) {
                result.addAll(visibleProperty.getRequiredHelperObjects());
            }
            if (visibleProperty.getFinalDependencies() != null) {
                for (ObjectInfo finalDependency : visibleProperty.getFinalDependencies()) {
                    result.addAll(getInitRequiredHelperObjects(finalDependency));
                }
            }
            if (visibleProperty.getFinalValue().getObjectInfo() != null) {
                result.addAll(getVisiblePropsRequiredHelperObjects(visibleProperty.getFinalValue().getObjectInfo()));
            }
        }
        return result;
    }

    public List<String> getObjectsInit(List<ObjectInfo> objectInfos) {
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectInfo objectInfo : objectInfos) {
            allObjectsInit.addAll(getObjectsInit(objectInfo));
        }
        return allObjectsInit;
    }

    public List<String> getObjectsInit(ObjectInfo objectInfo) {
        if (objectInfo.isInitAdded()) {
            // to avoid double init
            return new ArrayList<>();
        }
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectInfo dependency : objectInfo.getInitDependencies()) {
            allObjectsInit.addAll(getObjectsInit(dependency));
        }
        if (!objectInfo.getInitCode().equals("")) {
            allObjectsInit.add(objectInfo.getInitCode());
        }
        objectInfo.setInitAdded(true);
        return allObjectsInit;
    }
}
