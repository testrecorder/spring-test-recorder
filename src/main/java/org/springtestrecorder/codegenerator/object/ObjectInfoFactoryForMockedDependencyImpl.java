/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.object;

import org.springtestrecorder.analyzer.methodrun.DependencyMethodRunInfo;
import org.springtestrecorder.aspect.RecordMockForTest;
import org.springtestrecorder.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectInfoFactoryForMockedDependencyImpl extends ObjectInfoFactory {
    private final ObjectInfoFactoryManager objectInfoFactoryManager;

    public ObjectInfoFactoryForMockedDependencyImpl(ObjectInfoFactoryManager objectInfoFactoryManager) {
        this.objectInfoFactoryManager = objectInfoFactoryManager;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (context.getObject().getClass().isAnnotationPresent(RecordMockForTest.class)) {
            ObjectInfo objectInfo = new ObjectInfo(context, context.getObjectName());

            objectInfo.declareRequiredImports = Arrays.asList(
                    "static org.mockito.Mockito.*",
                    context.getObject().getClass().getName());

            objectInfo.toRunAfterMethodRun = () -> {
                List<DependencyMethodRunInfo> dependencyMethodRuns =
                        context.getTestGenerator().getDependencyMethodRuns().stream()
                                .filter(x -> x.getTarget().getClass() == context.getObject().getClass())
                                .collect(Collectors.toList());
                objectInfo.initDependencies = getDependencies(context, dependencyMethodRuns);

                objectInfo.initCode = getInitCode(context, dependencyMethodRuns);
            };

            return objectInfo;
        } else {
            return null;
        }
    }

    private List<ObjectInfo> getDependencies(ObjectInfoCreationContext context,
                                             List<DependencyMethodRunInfo> dependencyMethodRuns) {
        List<ObjectInfo> allArgs = dependencyMethodRuns.stream()
                .flatMap(x -> x.getArguments().stream())
                .map(arg -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), arg))
                .collect(Collectors.toList());

        List<ObjectInfo> allResults = dependencyMethodRuns.stream()
                .map(DependencyMethodRunInfo::getResult)
                .map(result -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), result))
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
                .map(arg -> objectInfoFactoryManager.getCommonObjectInfo(context.getTestGenerator(), arg))
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.joining(", "));

        if (dependencyMethodRunInfo.getException() != null) {
            return new StringGenerator()
                    .setTemplate("doThrow({{exceptionClassName}}.class)\n" + "" +
                            "    .when({{objectName}}).{{methodName}}({{methodArgsInline}});\n")
                    .addAttribute("exceptionClassName", dependencyMethodRunInfo.getException().getClass().getSimpleName())
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("methodName", dependencyMethodRunInfo.getMethodName())
                    .addAttribute("methodArgsInline", methodArgsInline)
                    .generate();
        } else {
            ObjectInfo resultObjectInfo = objectInfoFactoryManager
                    .getCommonObjectInfo(context.getTestGenerator(), dependencyMethodRunInfo.getResult());
            String thenClause = new StringGenerator()
                        .setTemplate(".thenReturn({{resultInlineCode}})")
                        .addAttribute("resultInlineCode", resultObjectInfo.getInlineCode())
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
