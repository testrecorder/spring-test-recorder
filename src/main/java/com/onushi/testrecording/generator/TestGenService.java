package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

// TODO IB use a templating engine here. see https://www.baeldung.com/thymeleaf-generate-pdf
@Service
public class TestGenService {
    private final ClassInfoService classInfoService;

    public TestGenService(ClassInfoService classInfoService) {
        this.classInfoService = classInfoService;
    }

    public String generateTestString(TestGenInfo testGenInfo) {
        return getBeginMarkerString() +
                getPackageString(testGenInfo) +
                getImportsString(testGenInfo) +
                "\n" +
                getClassAndTestString(testGenInfo) +
                getEndMarkerString();
    }

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(TestGenInfo testGenInfo) {
        return String.format("package %s;%n%n", testGenInfo.getPackageName());
    }

    private String getImportsString(TestGenInfo testGenInfo) {
        return testGenInfo.getRequiredImports().stream()
                .map(x -> String.format("import %s;%n", x))
                .collect(Collectors.joining(""));
    }

    private StringBuilder getClassAndTestString(TestGenInfo testGenInfo) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("class %sTest {%n", testGenInfo.getShortClassName()));
        stringBuilder.append(String.format("    @Test%n"));
        stringBuilder.append(String.format("    void %s() throws Exception {%n", testGenInfo.getMethodName()));

        stringBuilder.append(String.format("        // Arrange%n"));
        stringBuilder.append(testGenInfo.getRequiredHelperObjects().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining("")));
        stringBuilder.append(testGenInfo.getObjectsInit().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining("")));
        stringBuilder.append(String.format("        %s %s = new %s();%n", testGenInfo.getShortClassName(),
                testGenInfo.getTargetObjectInfo().getObjectName(), testGenInfo.getShortClassName()));
        stringBuilder.append("\n");

        stringBuilder.append(String.format("        // Act%n"));
        stringBuilder.append(String.format("        %s result = %s.%s(%s);%n",
                classInfoService.getShortClassName(testGenInfo.getResultObjectInfo().getObject()),
                testGenInfo.getTargetObjectInfo().getObjectName(),
                testGenInfo.getMethodName(),
                String.join(", ", testGenInfo.getArgumentsInlineCode())));
        stringBuilder.append("\n");

        stringBuilder.append(String.format("        // Assert%n"));
        if (!testGenInfo.getResultInit().equals("")) {
            stringBuilder.append(String.format("        %s%n", testGenInfo.getResultInit()));
        }
        stringBuilder.append(String.format("        assertEquals(result, %s);%n",
                testGenInfo.getResultObjectInfo().getInlineCode()));

        stringBuilder.append(String.format("    }%n"));
        stringBuilder.append(String.format("}%n"));
        return stringBuilder;
    }



    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
