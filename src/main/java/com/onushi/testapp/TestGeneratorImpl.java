package com.onushi.testapp;

import org.springframework.stereotype.Component;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class TestGeneratorImpl implements TestGenerator {

    // TODO IB this should also have tests
    // TODO IB I should group tests for the same class together
    @Override
    public String getTestString(TestRunDto testRunDto) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getBeginMarkerString());
        stringBuilder.append(getPackageString(testRunDto.getPackageName()));
        stringBuilder.append(getImportsString());
        stringBuilder.append(getClassAndTestString(testRunDto));
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

    private StringBuilder getClassAndTestString(TestRunDto testRunDto) {
        StringBuilder stringBuilder = new StringBuilder();
        String classNameVar = testRunDto.getClassName().substring(0,1).toLowerCase(Locale.ROOT) + testRunDto.getClassName().substring(1);
        String argumentsText = "";
        if (testRunDto.getArguments().size() > 0) {
            argumentsText = testRunDto.getArguments().stream().map(ObjectDto::getValue).collect(Collectors.joining(", "));
        }

        stringBuilder.append(String.format("class %sTest {%n", testRunDto.getClassName()));
        stringBuilder.append(String.format("    @Test%n"));
        stringBuilder.append(String.format("    void %s() throws Exception {%n", testRunDto.getMethodName()));
        stringBuilder.append(String.format("        %s %s = new %s();%n", testRunDto.getClassName(), classNameVar, testRunDto.getClassName()));
        stringBuilder.append(String.format("        assertEquals(%s.%s(%s), %s);%n",
                classNameVar, testRunDto.getMethodName(), argumentsText, testRunDto.getResult().getValue()));
        stringBuilder.append(String.format("    }%n"));
        stringBuilder.append(String.format("}%n"));
        return stringBuilder;
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
