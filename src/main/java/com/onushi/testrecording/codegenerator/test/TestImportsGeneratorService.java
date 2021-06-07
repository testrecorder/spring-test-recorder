package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.object.VisibleProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestImportsGeneratorService {
    public String getImportsString(TestGenerator testGenerator) {
        return getRequiredImports(testGenerator).stream()
                .map(x -> String.format("import %s;%n", x))
                .collect(Collectors.joining(""));
    }

    private List<String> getRequiredImports(TestGenerator testGenerator) {
        List<String> result = new ArrayList<>();
        result.add("org.junit.jupiter.api.Test");
        result.add("static org.junit.jupiter.api.Assertions.*");

        List<ObjectInfo> objectsToInit = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToInit.add(testGenerator.targetObjectInfo);

        result.addAll(objectsToInit.stream()
                .flatMap(x -> getDeclareAndInitRequiredImports(x).stream())
                .collect(Collectors.toList()));

        result.addAll(testGenerator.getExpectedResultObjectInfo().getDeclareRequiredImports());

        result.addAll(getVisiblePropsRequiredImports(testGenerator.getExpectedResultObjectInfo()));

        result = result.stream()
                .distinct()
                .filter(x -> !x.startsWith(testGenerator.getPackageName() + "."))
                .collect(Collectors.toList());
        return result;
    }

    private List<String> getDeclareAndInitRequiredImports(ObjectInfo objectInfo) {
        List<String> result = new ArrayList<>(objectInfo.getDeclareRequiredImports());
        result.addAll(objectInfo.getInitRequiredImports());
        for (ObjectInfo initDependency : objectInfo.getInitDependencies()) {
            result.addAll(getDeclareAndInitRequiredImports(initDependency));
        }
        return result;
    }

    private List<String> getVisiblePropsRequiredImports(ObjectInfo objectInfo) {
        List<String> result = new ArrayList<>();
        for (String key : objectInfo.getVisibleProperties().keySet()) {
            VisibleProperty visibleProperty = objectInfo.getVisibleProperties().get(key);
            if (visibleProperty.getRequiredImports() != null) {
                result.addAll(visibleProperty.getRequiredImports());
            }
            if (visibleProperty.getFinalDependencies() != null) {
                for (ObjectInfo finalDependency : visibleProperty.getFinalDependencies()) {
                    result.addAll(getDeclareAndInitRequiredImports(finalDependency));
                }
            }
            if (visibleProperty.getFinalValue().getObjectInfo() != null) {
                result.addAll(getVisiblePropsRequiredImports(visibleProperty.getFinalValue().getObjectInfo()));
            }
        }
        return result;
    }
}
