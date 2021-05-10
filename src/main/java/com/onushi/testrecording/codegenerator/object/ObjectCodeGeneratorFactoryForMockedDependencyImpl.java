package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryForMockedDependencyImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;

    public ObjectCodeGeneratorFactoryForMockedDependencyImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {

        // TODO IB !!!! precompute some of these in TestGenerator
        List<DependencyMethodRunInfo> dependencyMethodRuns = context.getTestGenerator().getDependencyMethodRuns();
        List<DependencyMethodRunInfo> dependencyMethodRunForObjectClass = dependencyMethodRuns.stream()
                .filter(x -> x.getTarget().getClass() == context.getObject().getClass())
                .collect(Collectors.toList());
        if (dependencyMethodRunForObjectClass.size() > 0) {
            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

            objectCodeGenerator.requiredImports = Arrays.asList("static org.mockito.ArgumentMatchers.any",
                    "static org.mockito.Mockito.mock",
                    "static org.mockito.Mockito.when",
                    context.getObject().getClass().getName());

            List<ObjectCodeGenerator> allResults = dependencyMethodRunForObjectClass.stream()
                    .map(DependencyMethodRunInfo::getResult)
                    .map(result -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), result))
                    .collect(Collectors.toList());

            objectCodeGenerator.dependencies = allResults.stream()
                    .distinct()
                    .collect(Collectors.toList());


            objectCodeGenerator.initCode = getInitCode(context, dependencyMethodRunForObjectClass);

            return objectCodeGenerator;
        } else {
            return null;
        }
    }

    private String getInitCode(ObjectCodeGeneratorCreationContext context, List<DependencyMethodRunInfo> dependencyMethodRunForObjectClass) {
        // TODO IB !!!! hard-coded
        DependencyMethodRunInfo dependencyMethodRunInfo = dependencyMethodRunForObjectClass.get(0);
        String methodArgsMatchers = "any(int.class))";
        ObjectCodeGenerator resultCodeGenerator = objectCodeGeneratorFactoryManager
                .getCommonObjectCodeGenerator(context.getTestGenerator(), dependencyMethodRunInfo.getResult());
        String thenReturns = new StringGenerator()
                .setTemplate(".thenReturn({{resultInlineCode}})")
                .addAttribute("resultInlineCode", resultCodeGenerator.getInlineCode())
                .generate();

        String whenClauses = new StringGenerator()
                .setTemplate("when({{objectName}}.{{methodName}}({{methodArgsMatchers}}){{thenReturns}};\n")
                .addAttribute("objectName", context.getObjectName())
                .addAttribute("methodName", dependencyMethodRunInfo.getMethodName())
                .addAttribute("methodArgsMatchers", methodArgsMatchers)
                .addAttribute("thenReturns", thenReturns)
                .generate();

        return new StringGenerator()
                .setTemplate("{{shortClassName}} {{objectName}} = mock({{shortClassName}}.class);\n" +
                        "{{whenClauses}}")
                .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                .addAttribute("objectName", context.getObjectName())
                .addAttribute("whenClauses", whenClauses)
                .generate();
    }
}
