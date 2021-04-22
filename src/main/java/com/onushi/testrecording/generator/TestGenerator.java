package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.test.TestInfo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

// TODO IB use a templating engine here. see https://www.baeldung.com/thymeleaf-generate-pdf
// TODO IB Write a test for this
// TODO IB !!!! Create a TestGenInfo in this package
@Service
public class TestGenerator {

    public String getTestString(TestInfo testInfo) {
        return getBeginMarkerString() +
                getPackageString(testInfo) +
                getImportsString(testInfo) +
                "\n" +
                getClassAndTestString(testInfo) +
                getEndMarkerString();
    }

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(TestInfo testInfo) {
        return String.format("package %s;%n%n", testInfo.getPackageName());
    }

    private String getImportsString(TestInfo testInfo) {
        return testInfo.getRequiredImports().stream()
                .map(x -> String.format("import %s;%n", x))
                .collect(Collectors.joining(""));
    }

    private StringBuilder getClassAndTestString(TestInfo testInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        String argumentsRequiredHelperObjects = testInfo.getRequiredHelperObjects().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining(""));
        String argumentsInit = testInfo.getObjectsInit().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining(""));
        String argumentsCode = String.join(", ", testInfo.getArgumentsInlineCode());

        // TODO IB create a result variable
        // TODO IB move expectedResult at end

        stringBuilder.append(String.format("class %sTest {%n", testInfo.getShortClassName()));
        stringBuilder.append(String.format("    @Test%n"));
        stringBuilder.append(String.format("    void %s() throws Exception {%n", testInfo.getMethodName()));
        stringBuilder.append(argumentsRequiredHelperObjects);
        stringBuilder.append(argumentsInit);
        stringBuilder.append(String.format("        %s %s = new %s();%n", testInfo.getShortClassName(), testInfo.getTargetObjectName(), testInfo.getShortClassName()));
        stringBuilder.append(String.format("        assertEquals(%s.%s(%s), %s);%n",
                testInfo.getTargetObjectName(), testInfo.getMethodName(), argumentsCode, testInfo.getResultObjectInfo().getInlineCode()));
        stringBuilder.append(String.format("    }%n"));
        stringBuilder.append(String.format("}%n"));
        return stringBuilder;
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
