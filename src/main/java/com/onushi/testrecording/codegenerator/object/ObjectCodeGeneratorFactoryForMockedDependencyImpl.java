package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.methodrun.DependencyMethodRunInfo;

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


            return objectCodeGenerator;
        } else {
            return null;
        }
    }
}
