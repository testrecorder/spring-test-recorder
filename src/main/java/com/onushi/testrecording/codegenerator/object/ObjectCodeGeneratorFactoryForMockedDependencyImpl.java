package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.ArrayList;
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


            List<ObjectCodeGenerator> allArgs = dependencyMethodRunForObjectClass.stream()
                    .flatMap(x -> x.getArguments().stream())
                    .map(arg -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), arg))
                    .collect(Collectors.toList());

            List<ObjectCodeGenerator> allResults = dependencyMethodRunForObjectClass.stream()
                    .map(DependencyMethodRunInfo::getResult)
                    .map(result -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), result))
                    .collect(Collectors.toList());

            List<ObjectCodeGenerator> allDependencies = new ArrayList<>(allArgs);
            allDependencies.addAll(allResults);

            objectCodeGenerator.dependencies = allDependencies.stream()
                    .distinct()
                    .collect(Collectors.toList());

            objectCodeGenerator.initCode = getInitCode(context, dependencyMethodRunForObjectClass);

            return objectCodeGenerator;
        } else {
            return null;
        }
    }

    private String getInitCode(ObjectCodeGeneratorCreationContext context, List<DependencyMethodRunInfo> dependencyMethodRuns) {
        String whenClauses = dependencyMethodRuns.stream()
                .map(dependencyMethodRunInfo -> getWhenClause(context, dependencyMethodRunInfo))
                .collect(Collectors.joining(""));

        return new StringGenerator()
                .setTemplate("{{shortClassName}} {{objectName}} = mock({{shortClassName}}.class);\n" +
                        "{{whenClauses}}")
                .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                .addAttribute("objectName", context.getObjectName())
                .addAttribute("whenClauses", whenClauses)
                .generate();
    }

    private String getWhenClause(ObjectCodeGeneratorCreationContext context, DependencyMethodRunInfo dependencyMethodRunInfo) {
        String methodArgsInline = dependencyMethodRunInfo.getArguments().stream()
                .map(arg -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), arg))
                .map(ObjectCodeGenerator::getInlineCode)
                .collect(Collectors.joining(", "));

        ObjectCodeGenerator resultCodeGenerator = objectCodeGeneratorFactoryManager
                .getCommonObjectCodeGenerator(context.getTestGenerator(), dependencyMethodRunInfo.getResult());
        String thenReturn = new StringGenerator()
                .setTemplate(".thenReturn({{resultInlineCode}})")
                .addAttribute("resultInlineCode", resultCodeGenerator.getInlineCode())
                .generate();

        return new StringGenerator()
                .setTemplate("when({{objectName}}.{{methodName}}({{methodArgsMatchers}}){{thenReturn}};\n")
                .addAttribute("objectName", context.getObjectName())
                .addAttribute("methodName", dependencyMethodRunInfo.getMethodName())
                .addAttribute("methodArgsMatchers", methodArgsInline)
                .addAttribute("thenReturn", thenReturn)
                .generate();
    }
}
