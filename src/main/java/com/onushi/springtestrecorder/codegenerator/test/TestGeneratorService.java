/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TestGeneratorService {
    private final TestImportsGeneratorService testImportsGeneratorService;
    private final TestArrangeGeneratorService testArrangeGeneratorService;
    private final TestActGeneratorService testActGeneratorService;
    private final TestAssertGeneratorService testAssertGeneratorService;

    public TestGeneratorService(TestImportsGeneratorService testImportsGeneratorService,
                                TestArrangeGeneratorService testArrangeGeneratorService,
                                TestActGeneratorService testActGeneratorService,
                                TestAssertGeneratorService testAssertGeneratorService) {
        this.testImportsGeneratorService = testImportsGeneratorService;
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
        return new StringGenerator()
            .setTemplate(
                "class {{testClassName}} {\n" +
                getTestGeneratedDateTime() +
                COMMENT_BEFORE_TEST +
                "    @Test\n" +
                "    void {{methodName}}() throws Exception {\n" +
                        testArrangeGeneratorService.getArrangeCode(testGenerator) +
                        testActGeneratorService.getActCode(testGenerator) +
                        testAssertGeneratorService.getResultAssertCode(testGenerator) +
                        testAssertGeneratorService.getSideEffectsAssertCode(testGenerator) +
                "    }\n" +
                "}\n")
            .addAttribute("testClassName", testGenerator.getShortClassName() + "Test")
            .addAttribute("methodName", testGenerator.getMethodName())
            .generate();
    }

    private String getTestGeneratedDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return new StringGenerator()
                .setTemplate("    //Test Generated on {{datetime}} with @RecordTest\n")
                .addAttribute("datetime", simpleDateFormat.format(new Date()))
                .generate();
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
