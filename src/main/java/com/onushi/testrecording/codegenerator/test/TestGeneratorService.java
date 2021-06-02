package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGenerator;
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

        List<ObjectCodeGenerator> objectsToInit = new ArrayList<>(testGenerator.argumentObjectCodeGenerators);
        objectsToInit.add(testGenerator.targetObjectCodeGenerator);
        attributes.put("objectsInit", getObjectsInit(objectsToInit).stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")));

        attributes.put("targetObjectName", testGenerator.getTargetObjectCodeGenerator().getObjectName());

        attributes.put("resultDeclareClassName", testGenerator.getResultDeclareClassName());

        attributes.put("argumentsInlineCode", String.join(", ", testGenerator.getArgumentsInlineCode()));
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

    private String getAssertCode(TestGenerator testGenerator, Map<String, String> attributes) throws InvocationTargetException, IllegalAccessException {
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
    // TODO IB !!!! do I need the attributes?
    private String getAssertCode(TestGenerator testGenerator, Map<String, String> attributes,
                                 ObjectCodeGenerator objectCodeGenerator, String assertPath) throws InvocationTargetException, IllegalAccessException {
        if (objectCodeGenerator.getObject() == null) {
            return "        assertNull(result);\n";
        } else if (objectCodeGenerator.isCanUseDoubleEqualForComparison()) {
            return new StringGenerator()
                    .addAttribute("assertPath", assertPath)
                    .addAttribute("inlineCode", objectCodeGenerator.getInlineCode())
                    .setTemplate("        assertEquals({{inlineCode}}, {{assertPath}});\n")
                    .generate();
        } else if (classInfoService.hasEquals(objectCodeGenerator.getObject().getClass())) {
            return new StringGenerator()
                    .addAttribute("objectsInit", getObjectsInit(Collections.singletonList(objectCodeGenerator)).stream()
                            .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n").collect(Collectors.joining("")))
                    .addAttribute("assertPath", assertPath)
                    .addAttribute("inlineCode", objectCodeGenerator.getInlineCode())
                    .setTemplate("{{objectsInit}}" +
                        "        assertEquals({{inlineCode}}, {{assertPath}});\n")
                    .generate();
        } else if (objectCodeGenerator.getObject().getClass().getName().startsWith("[")) {
            return getAssertForCollection(testGenerator, attributes, objectCodeGenerator, assertPath, "[{{index}}]", ".length");
        } if (objectCodeGenerator.getObject() instanceof List<?>) {
            return getAssertForCollection(testGenerator, attributes, objectCodeGenerator, assertPath, ".get({{index}})", ".size()");
        // TODO IB !!!! continue here with other known types
        } else {
            List<Method> publicGetters = classInfoService.getPublicGetters(objectCodeGenerator.getObject().getClass());
            for (Method publicGetter : publicGetters) {
                Object value = publicGetter.invoke(objectCodeGenerator.getObject());
                // TODO IB !!!! call this
                // TODO IB !!!! do I have the dependents of this object?
                // objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(testGenerator, value)
            }

            List<Field> publicFields = classInfoService.getPublicFields(objectCodeGenerator.getObject().getClass());
            for (Field publicField : publicFields) {
                Object value = publicField.get(objectCodeGenerator.getObject());
            }

            return new StringGenerator()
                    .addAttribute("assertPath", assertPath)
                    .setTemplate("        // TODO Add assert for {{assertPath}} object \n")
                    .generate();
        }
    }

    private String getAssertForCollection(TestGenerator testGenerator,
                                          Map<String, String> attributes,
                                          ObjectCodeGenerator objectCodeGenerator,
                                          String assertPath,
                                          String indexSyntax,
                                          String sizeSyntax) throws InvocationTargetException, IllegalAccessException {
        List<ObjectCodeGenerator> elements = objectCodeGenerator.getElements();
        StringBuilder elementsAssert = new StringBuilder();
        for (int i = 0, elementsSize = elements.size(); i < elementsSize; i++) {
            ObjectCodeGenerator element = elements.get(i);
            String elementAssertPath =  new StringGenerator()
                    .addAttribute("index", i)
                    .addAttribute("assertPath", assertPath)
                    .setTemplate("{{assertPath}}" + indexSyntax)
                    .generate();
            elementsAssert.append(getAssertCode(testGenerator, attributes, element, elementAssertPath));
        }
        return new StringGenerator()
                .addAttribute("size", elements.size())
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

    public List<String> getObjectsInit(List<ObjectCodeGenerator> objectCodeGenerators) {
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectCodeGenerator objectCodeGenerator : objectCodeGenerators) {
            allObjectsInit.addAll(getObjectsInit(objectCodeGenerator));
        }
        return allObjectsInit;
    }

    public List<String> getObjectsInit(ObjectCodeGenerator objectCodeGenerator) {
        if (objectCodeGenerator.isInitPrepared() || objectCodeGenerator.isInitDone()) {
            // to avoid cyclic traversal
            return new ArrayList<>();
        }
        objectCodeGenerator.setInitPrepared(true);
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectCodeGenerator dependency : objectCodeGenerator.getDependencies()) {
            allObjectsInit.addAll(getObjectsInit(dependency));
        }
        if (!objectCodeGenerator.getInitCode().equals("")) {
            allObjectsInit.add(objectCodeGenerator.getInitCode());
        }
        objectCodeGenerator.setInitDone(true);
        return allObjectsInit;
    }
}
