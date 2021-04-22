package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.testrun.TestRunInfo;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

// TODO IB use a templating engine here. see https://www.baeldung.com/thymeleaf-generate-pdf
// TODO IB Write a test for this
@Component
public class TestGenerator {

    public String getTestString(TestRunInfo testRunInfo) {
        return getBeginMarkerString() +
                getPackageString(testRunInfo) +
                getImportsString(testRunInfo) +
                "\n" +
                getClassAndTestString(testRunInfo) +
                getEndMarkerString();
    }

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(TestRunInfo testRunInfo) {
        return String.format("package %s;%n%n", testRunInfo.getPackageName());
    }

    private String getImportsString(TestRunInfo testRunInfo) {
        return testRunInfo.getRequiredImports().stream()
                .map(x -> String.format("import %s;%n", x))
                .collect(Collectors.joining(""));
    }

    private StringBuilder getClassAndTestString(TestRunInfo testRunInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        String argumentsRequiredHelperObjects = testRunInfo.getRequiredHelperObjects().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining(""));
        String argumentsInit = testRunInfo.getObjectsInit().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining(""));
        String argumentsCode = String.join(", ", testRunInfo.getArgumentsInlineCode());

        // TODO IB create a result variable
        // TODO IB move expectedResult at end

        stringBuilder.append(String.format("class %sTest {%n", testRunInfo.getShortClassName()));
        stringBuilder.append(String.format("    @Test%n"));
        stringBuilder.append(String.format("    void %s() throws Exception {%n", testRunInfo.getMethodName()));
        stringBuilder.append(argumentsRequiredHelperObjects);
        stringBuilder.append(argumentsInit);
        stringBuilder.append(String.format("        %s %s = new %s();%n", testRunInfo.getShortClassName(), testRunInfo.getTargetObjectName(), testRunInfo.getShortClassName()));
        stringBuilder.append(String.format("        assertEquals(%s.%s(%s), %s);%n",
                testRunInfo.getTargetObjectName(), testRunInfo.getMethodName(), argumentsCode, testRunInfo.getResultObjectInfo().getInlineCode()));
        stringBuilder.append(String.format("    }%n"));
        stringBuilder.append(String.format("}%n"));
        return stringBuilder;
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
