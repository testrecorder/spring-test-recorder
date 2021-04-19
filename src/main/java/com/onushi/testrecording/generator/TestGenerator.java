package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.ObjectInfo;
import com.onushi.testrecording.analizer.TestRunInfo;
import org.springframework.stereotype.Component;
import java.util.Locale;
import java.util.stream.Collectors;

// TODO IB use a templating engine here
@Component
public class TestGenerator {

    // TODO IB this should also have tests
    // TODO IB I should group tests for the same class together
    public String getTestString(TestRunInfo testRunInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getBeginMarkerString());
        stringBuilder.append(getPackageString(testRunInfo.getPackageName()));
        stringBuilder.append(getImportsString());
        stringBuilder.append(getClassAndTestString(testRunInfo));
        stringBuilder.append(getEndMarkerString());
        return stringBuilder.toString();
    }

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(String packageName) {
        return String.format("package %s;%n%n", packageName);
    }

    private StringBuilder getImportsString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("import org.junit.jupiter.api.Test;%n%n"));
        stringBuilder.append(String.format("import static org.junit.jupiter.api.Assertions.*;%n%n"));
        return stringBuilder;
    }

    private StringBuilder getClassAndTestString(TestRunInfo testRunInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        String classNameVar = testRunInfo.getClassName().substring(0,1).toLowerCase(Locale.ROOT) + testRunInfo.getClassName().substring(1);
        String argumentsText = "";
        if (testRunInfo.getArguments().size() > 0) {
            argumentsText = testRunInfo.getArguments().stream().map(ObjectInfo::getValue).collect(Collectors.joining(", "));
        }

        stringBuilder.append(String.format("class %sTest {%n", testRunInfo.getClassName()));
        stringBuilder.append(String.format("    @Test%n"));
        stringBuilder.append(String.format("    void %s() throws Exception {%n", testRunInfo.getMethodName()));
        stringBuilder.append(String.format("        %s %s = new %s();%n", testRunInfo.getClassName(), classNameVar, testRunInfo.getClassName()));
        stringBuilder.append(String.format("        assertEquals(%s.%s(%s), %s);%n",
                classNameVar, testRunInfo.getMethodName(), argumentsText, testRunInfo.getTestResult().getValue()));
        stringBuilder.append(String.format("    }%n"));
        stringBuilder.append(String.format("}%n"));
        return stringBuilder;
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
