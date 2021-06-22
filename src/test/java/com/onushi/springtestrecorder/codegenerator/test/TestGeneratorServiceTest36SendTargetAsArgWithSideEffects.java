package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.services.ServiceWithState;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest36SendTargetAsArgWithSideEffects extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        ServiceWithState serviceWithState = new ServiceWithState(5);
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(serviceWithState)
                .methodName("changeService")
                .arguments(Collections.singletonList(serviceWithState))
                .fallBackResultType(void.class)
                .build());
        serviceWithState.changeService(serviceWithState);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class ServiceWithStateTest {\n" +
                        "    //TODO rename the test to describe the use case\n" +
                        "    //TODO refactor the generated code to make it easier to understand\n" +
                        "    @Test\n" +
                        "    void changeService() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        ServiceWithState serviceWithState = new ServiceWithState(5);\n" +
                        "\n" +
                        "        // Act\n" +
                        "        serviceWithState.changeService(serviceWithState);\n" +
                        "\n" +
                        "\n" +
                        "        // Side Effects\n" +
                        "        assertEquals(10, serviceWithState.getSampleInt());\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void changeService() {
        // Arrange
        ServiceWithState serviceWithState = new ServiceWithState(5);

        // Act
        serviceWithState.changeService(serviceWithState);


        // Side Effects
        assertEquals(10, serviceWithState.getSampleInt());
    }
}
