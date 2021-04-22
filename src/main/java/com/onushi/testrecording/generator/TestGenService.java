package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.test.MethodRunInfo;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

// TODO IB use a templating engine here. see https://www.baeldung.com/thymeleaf-generate-pdf
// TODO IB Write tests for the classes that don't have test
@Service
public class TestGenService {

    public String getTestString(MethodRunInfo methodRunInfo) {
        return getBeginMarkerString() +
                getPackageString(methodRunInfo) +
                getImportsString(methodRunInfo) +
                "\n" +
                getClassAndTestString(methodRunInfo) +
                getEndMarkerString();
    }

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(MethodRunInfo methodRunInfo) {
        return String.format("package %s;%n%n", methodRunInfo.getPackageName());
    }

    private String getImportsString(MethodRunInfo methodRunInfo) {
        return methodRunInfo.getRequiredImports().stream()
                .map(x -> String.format("import %s;%n", x))
                .collect(Collectors.joining(""));
    }

    private StringBuilder getClassAndTestString(MethodRunInfo methodRunInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        String argumentsRequiredHelperObjects = methodRunInfo.getRequiredHelperObjects().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining(""));
        String argumentsInit = methodRunInfo.getObjectsInit().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining(""));
        String argumentsCode = String.join(", ", methodRunInfo.getArgumentsInlineCode());

        // TODO IB create a result variable
        // TODO IB move expectedResult at end

        stringBuilder.append(String.format("class %sTest {%n", methodRunInfo.getShortClassName()));
        stringBuilder.append(String.format("    @Test%n"));
        stringBuilder.append(String.format("    void %s() throws Exception {%n", methodRunInfo.getMethodName()));
        stringBuilder.append(argumentsRequiredHelperObjects);
        stringBuilder.append(argumentsInit);
        stringBuilder.append(String.format("        %s %s = new %s();%n", methodRunInfo.getShortClassName(), methodRunInfo.getTargetObjectName(), methodRunInfo.getShortClassName()));
        stringBuilder.append(String.format("        assertEquals(%s.%s(%s), %s);%n",
                methodRunInfo.getTargetObjectName(), methodRunInfo.getMethodName(), argumentsCode, methodRunInfo.getResultObjectInfo().getInlineCode()));
        stringBuilder.append(String.format("    }%n"));
        stringBuilder.append(String.format("}%n"));
        return stringBuilder;
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
