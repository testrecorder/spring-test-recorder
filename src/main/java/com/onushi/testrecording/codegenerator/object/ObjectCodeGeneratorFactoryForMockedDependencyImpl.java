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
        List<DependencyMethodRunInfo> dependencyMethodRuns =
                context.getTestGenerator().getDependencyMethodRuns().stream()
                .filter(x -> x.getTarget().getClass() == context.getObject().getClass())
                .collect(Collectors.toList());
        if (dependencyMethodRuns.size() > 0) {
            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

            objectCodeGenerator.requiredImports = Arrays.asList(
                    "static org.mockito.Mockito.mock",
                    "static org.mockito.Mockito.when",
                    context.getObject().getClass().getName());

            objectCodeGenerator.dependencies = getDependencies(context, dependencyMethodRuns);

            objectCodeGenerator.initCode = getInitCode(context, dependencyMethodRuns);

            return objectCodeGenerator;
        } else {
            return null;
        }
    }

    private List<ObjectCodeGenerator> getDependencies(ObjectCodeGeneratorCreationContext context,
                                                      List<DependencyMethodRunInfo> dependencyMethodRuns) {
        List<ObjectCodeGenerator> allArgs = dependencyMethodRuns.stream()
                .flatMap(x -> x.getArguments().stream())
                .map(arg -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), arg))
                .collect(Collectors.toList());

        List<ObjectCodeGenerator> allResults = dependencyMethodRuns.stream()
                .map(DependencyMethodRunInfo::getResult)
                .map(result -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), result))
                .collect(Collectors.toList());

        List<ObjectCodeGenerator> allDependencies = new ArrayList<>(allArgs);
        allDependencies.addAll(allResults);

        return allDependencies.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private String getInitCode(ObjectCodeGeneratorCreationContext context, List<DependencyMethodRunInfo> dependencyMethodRuns) {
        String whenClauses = dependencyMethodRuns.stream()
                .map(dependencyMethodRunInfo -> getWhenClause(context, dependencyMethodRunInfo))
                .distinct()
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

        String thenClause;
        if (dependencyMethodRunInfo.getException() == null) {
            ObjectCodeGenerator resultCodeGenerator = objectCodeGeneratorFactoryManager
                    .getCommonObjectCodeGenerator(context.getTestGenerator(), dependencyMethodRunInfo.getResult());
            thenClause = new StringGenerator()
                    .setTemplate(".thenReturn({{resultInlineCode}})")
                    .addAttribute("resultInlineCode", resultCodeGenerator.getInlineCode())
                    .generate();
        } else {
            thenClause = new StringGenerator()
                    .setTemplate(".thenThrow({{exceptionClassName}}.class)")
                    .addAttribute("exceptionClassName", dependencyMethodRunInfo.getException().getClass().getSimpleName())
                    .generate();
        }

        return new StringGenerator()
                .setTemplate("when({{objectName}}.{{methodName}}({{methodArgsMatchers}})){{thenClause}};\n")
                .addAttribute("objectName", context.getObjectName())
                .addAttribute("methodName", dependencyMethodRunInfo.getMethodName())
                .addAttribute("methodArgsMatchers", methodArgsInline)
                .addAttribute("thenClause", thenClause)
                .generate();
    }
}
