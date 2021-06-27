/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import org.springtestrecorder.codegenerator.object.ObjectInfo;
import org.springtestrecorder.codegenerator.object.VisibleProperty;
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
            if (visibleProperty.getLastSnapshot().getRequiredImports() != null) {
                result.addAll(visibleProperty.getLastSnapshot().getRequiredImports());
            }
            if (visibleProperty.getLastSnapshot().getOtherDependencies() != null) {
                for (ObjectInfo finalDependency : visibleProperty.getLastSnapshot().getOtherDependencies()) {
                    result.addAll(getDeclareAndInitRequiredImports(finalDependency));
                }
            }
            if (visibleProperty.getLastSnapshot().getValue().getObjectInfo() != null) {
                result.addAll(getVisiblePropsRequiredImports(visibleProperty.getLastSnapshot().getValue().getObjectInfo()));
            }
        }
        return result;
    }
}
