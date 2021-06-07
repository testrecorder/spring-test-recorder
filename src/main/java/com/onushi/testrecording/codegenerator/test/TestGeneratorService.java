package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import com.onushi.testrecording.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestGeneratorService {
    private final StringService stringService;
    private final TestImportsGeneratorService testImportsGeneratorService;
    private final TestHelperObjectsGeneratorService testHelperObjectsGeneratorService;
    private final TestObjectsInitGeneratorService testObjectsInitGeneratorService;
    private final TestArrangeGeneratorService testArrangeGeneratorService;
    private final TestActGeneratorService testActGeneratorService;
    private final TestAssertGeneratorService testAssertGeneratorService;

    public TestGeneratorService(StringService stringService,
                                TestImportsGeneratorService testImportsGeneratorService,
                                TestHelperObjectsGeneratorService testHelperObjectsGeneratorService,
                                TestObjectsInitGeneratorService testObjectsInitGeneratorService,
                                TestArrangeGeneratorService testArrangeGeneratorService,
                                TestActGeneratorService testActGeneratorService,
                                TestAssertGeneratorService testAssertGeneratorService) {
        this.stringService = stringService;
        this.testImportsGeneratorService = testImportsGeneratorService;
        this.testHelperObjectsGeneratorService = testHelperObjectsGeneratorService;
        this.testObjectsInitGeneratorService = testObjectsInitGeneratorService;
        this.testArrangeGeneratorService = testArrangeGeneratorService;
        this.testActGeneratorService = testActGeneratorService;
        this.testAssertGeneratorService = testAssertGeneratorService;
    }

    public final String COMMENT_BEFORE_TEST =
            "    //TODO rename the test to describe the use case\n" +
            "    //TODO refactor the generated code to make it easier to understand\n";

    public String generateTestCode(TestGenerator testGenerator) {
        return getBeginMarkerString() +
                getPackageString(testGenerator) +
                this.testImportsGeneratorService.getImportsString(testGenerator) +
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

    private String getClassAndTestString(TestGenerator testGenerator) {
        StringGenerator stringGenerator = new StringGenerator();
        Map<String, String> attributes = getStringGeneratorAttributes(testGenerator);
        stringGenerator.setTemplate(
                "class {{testClassName}} {\n" +
                getTestGeneratedDateTime() +
                COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void {{methodName}}() throws Exception {\n" +
                        testArrangeGeneratorService.getArrangeCode(attributes) +
                        testActGeneratorService.getActCode(testGenerator, attributes) +
                        testAssertGeneratorService.getAssertCode(testGenerator) +
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
        attributes.put("requiredHelperObjects",
                this.testHelperObjectsGeneratorService.getRequiredHelperObjects(testGenerator).stream()
                        .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n")
                        .collect(Collectors.joining("")));

        List<ObjectInfo> objectsToInit = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToInit.add(testGenerator.targetObjectInfo);
        attributes.put("objectsInit", testObjectsInitGeneratorService.getObjectsInit(objectsToInit).stream()
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

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
