package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.object.VisibleProperty;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
                getAssertCode(testGenerator, testGenerator.getExpectedResultObjectInfo(), "result");
        }
    }

    private String getAssertCode(TestGenerator testGenerator,
                                 ObjectInfo objectInfo, String assertPath) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, VisibleProperty> entry : objectInfo.getVisibleProperties().entrySet()) {
            if (entry.getValue().getFinalValue().equals("null")) {
                stringBuilder.append("        assertNull(result);\n");
            } else {
                String assertString = new StringGenerator()
                        .setTemplate("        assertEquals({{expected}}, {{actual}});\n")
                        .addAttribute("expected", entry.getValue().getFinalValue())
                        .addAttribute("actual", assertPath + entry.getKey())
                        .generate();
                stringBuilder.append(assertString);
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
//        } else if (objectInfo.getObject().getClass().getName().startsWith("[")) {
//            return getAssertForCollection(testGenerator, attributes, objectInfo, assertPath, "[{{index}}]", ".length");
//        } if (objectInfo.getObject() instanceof List<?>) {
//            return getAssertForCollection(testGenerator, attributes, objectInfo, assertPath, ".get({{index}})", ".size()");
//        // TODO IB continue here with other known types
//        } else {
//            List<Method> publicGetters = classInfoService.getPublicGetters(objectInfo.getObject().getClass());
//            for (Method publicGetter : publicGetters) {
//                Object value = publicGetter.invoke(objectInfo.getObject());
//                // TODO IB call this
//                // TODO IB do I have the dependents of this object?
//                // objectInfoFactoryManager.getCommonObjectInfo(testGenerator, value)
//            }
//
//            List<Field> publicFields = classInfoService.getPublicFields(objectInfo.getObject().getClass());
//            for (Field publicField : publicFields) {
//                Object value = publicField.get(objectInfo.getObject());
//            }
//
//            return new StringGenerator()
//                    .addAttribute("assertPath", assertPath)
//                    .setTemplate("        // TODO Add assert for {{assertPath}} object \n")
//                    .generate();
//        }
//    }
//    private String getAssertForCollection(TestGenerator testGenerator,
//                                          Map<String, String> attributes,
//                                          ObjectInfo objectInfo,
//                                          String assertPath,
//                                          String indexSyntax,
//                                          String sizeSyntax) throws InvocationTargetException, IllegalAccessException {
//        List<ObjectInfo> elements = objectInfo.getElements();
//        StringBuilder elementsAssert = new StringBuilder();
//        for (int i = 0, elementsSize = elements.size(); i < elementsSize; i++) {
//            ObjectInfo element = elements.get(i);
//            String elementAssertPath =  new StringGenerator()
//                    .addAttribute("index", i)
//                    .addAttribute("assertPath", assertPath)
//                    .setTemplate("{{assertPath}}" + indexSyntax)
//                    .generate();
//            elementsAssert.append(getAssertCode(testGenerator, attributes, element, elementAssertPath));
//        }
//        return new StringGenerator()
//                .addAttribute("size", elements.size())
//                .addAttribute("assertPath", assertPath)
//                .addAttribute("elementsAssert", elementsAssert.toString())
//                .addAttribute("sizeSyntax", sizeSyntax)
//                .setTemplate(
//                        "        assertEquals({{size}}, {{assertPath}}" + sizeSyntax + ");\n" +
//                        "{{elementsAssert}}")
//                .generate();
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
