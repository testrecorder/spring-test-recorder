package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// TODO IB !!!! Plan
//      To create an object we must know
//          if it's @Component or not
//          the objects it depends on (from builder, setters, constructor)
//          also dependencies of dependencies. etc
//          keep all dependencies in the test to avoid creation of 2 ObjectCodeGenerator for the same object
//          n objects can have common dependencies
//              we need a dependency sorter
//              LATER - there will be a case when the dependencies are circular
//      Target dependencies @components will be mocked
//          Intercept calls to all dependent components and create when() calls
//      If target.method returns void or there is no equals
//          skip assertEquals(expectedResult, result)
//      If any property changed on target or target dependencies
//          create assertEquals() for all the new values

// TODO IB !!!! Explore
//      Generate a test with mocking
//      Dependencies of dependencies of dependencies
//      Exception thrown
//      Generics


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
                        getArrangeCode(attributes) +
                        getActCode(attributes) +
                        getAssertCode(attributes) +
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
        if (!testGenerator.getResultInit().equals("")) {
            attributes.put("expectedResultInit",
                    stringService.addPrefixOnAllLines(testGenerator.getResultInit(), "        ") + "\n");
        }
        attributes.put("expectedResult", testGenerator.getResultObjectCodeGenerator().getInlineCode());
        return attributes;
    }

    private String getArrangeCode(Map<String, String> attributes) {
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

    private String getActCode(Map<String, String> attributes) {
        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.addAttributes(attributes);
        stringGenerator.setTemplate(
            "        // Act\n" +
            "        {{resultType}} result = {{targetObjectName}}.{{methodName}}({{argumentsInlineCode}});\n\n"
        );

        return stringGenerator.generate();
    }

    private String getAssertCode(Map<String, String> attributes) {
        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.addAttributes(attributes);
        stringGenerator.setTemplate(
            "        // Assert\n" +
                    "{{expectedResultInit}}" +
            "        assertEquals({{expectedResult}}, result);\n");

        return stringGenerator.generate();
    }


    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
