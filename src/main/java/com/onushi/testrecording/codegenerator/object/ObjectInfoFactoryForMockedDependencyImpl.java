package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.methodrun.DependencyMethodRunInfo;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectInfoFactoryForMockedDependencyImpl extends ObjectInfoFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;

    public ObjectInfoFactoryForMockedDependencyImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
    }

    @Override
    public ObjectInfo createObjectCodeGenerator(ObjectInfoCreationContext context) {
        List<DependencyMethodRunInfo> dependencyMethodRuns =
                context.getTestGenerator().getDependencyMethodRuns().stream()
                .filter(x -> x.getTarget().getClass() == context.getObject().getClass())
                .collect(Collectors.toList());
        if (dependencyMethodRuns.size() > 0) {
            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName(), false);

            objectInfo.requiredImports = Arrays.asList(
                    "static org.mockito.Mockito.*",
                    context.getObject().getClass().getName());

            objectInfo.dependencies = getDependencies(context, dependencyMethodRuns);

            objectInfo.initCode = getInitCode(context, dependencyMethodRuns);

            return objectInfo;
        } else {
            return null;
        }
    }

    private List<ObjectInfo> getDependencies(ObjectInfoCreationContext context,
                                             List<DependencyMethodRunInfo> dependencyMethodRuns) {
        List<ObjectInfo> allArgs = dependencyMethodRuns.stream()
                .flatMap(x -> x.getArguments().stream())
                .map(arg -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), arg))
                .collect(Collectors.toList());

        List<ObjectInfo> allResults = dependencyMethodRuns.stream()
                .map(DependencyMethodRunInfo::getResult)
                .map(result -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), result))
                .collect(Collectors.toList());

        List<ObjectInfo> allDependencies = new ArrayList<>(allArgs);
        allDependencies.addAll(allResults);

        return allDependencies.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private String getInitCode(ObjectInfoCreationContext context, List<DependencyMethodRunInfo> dependencyMethodRuns) {
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

    private String getWhenClause(ObjectInfoCreationContext context, DependencyMethodRunInfo dependencyMethodRunInfo) {
        String methodArgsInline = dependencyMethodRunInfo.getArguments().stream()
                .map(arg -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), arg))
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.joining(", "));

        if (dependencyMethodRunInfo.getException() != null) {
            return new StringGenerator()
                    .setTemplate("doThrow({{exceptionClassName}}.class)\n" + "" +
                            "    .when({{objectName}}).{{methodName}}({{methodArgsInline}});")
                    .addAttribute("exceptionClassName", dependencyMethodRunInfo.getException().getClass().getSimpleName())
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("methodName", dependencyMethodRunInfo.getMethodName())
                    .addAttribute("methodArgsInline", methodArgsInline)
                    .generate();
        } else {
            ObjectInfo resultCodeGenerator = objectCodeGeneratorFactoryManager
                    .getCommonObjectCodeGenerator(context.getTestGenerator(), dependencyMethodRunInfo.getResult());
            String thenClause = new StringGenerator()
                        .setTemplate(".thenReturn({{resultInlineCode}})")
                        .addAttribute("resultInlineCode", resultCodeGenerator.getInlineCode())
                        .generate();
            return new StringGenerator()
                    .setTemplate("when({{objectName}}.{{methodName}}({{methodArgsInline}})){{thenClause}};\n")
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("methodName", dependencyMethodRunInfo.getMethodName())
                    .addAttribute("methodArgsInline", methodArgsInline)
                    .addAttribute("thenClause", thenClause)
                    .generate();
        }
    }
}
