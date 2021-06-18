package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.springtestrecorder.codegenerator.object.ObjectInfo;
import com.onushi.springtestrecorder.codegenerator.object.VisibleProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestHelperObjectsGeneratorService {
    public List<String> getRequiredHelperObjects(TestGenerator testGenerator) {
        List<String> result = new ArrayList<>();

        List<ObjectInfo> objectsToInit = new ArrayList<>(testGenerator.argumentObjectInfos);
        objectsToInit.add(testGenerator.targetObjectInfo);

        result.addAll(objectsToInit.stream()
                .flatMap(x -> getInitRequiredHelperObjects(x).stream())
                .collect(Collectors.toList()));

        result.addAll(getVisiblePropsRequiredHelperObjects(testGenerator.getExpectedResultObjectInfo()));

        return result.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getInitRequiredHelperObjects(ObjectInfo objectInfo) {
        List<String> result = new ArrayList<>(objectInfo.getInitRequiredHelperObjects());
        for (ObjectInfo initDependency : objectInfo.getInitDependencies()) {
            result.addAll(getInitRequiredHelperObjects(initDependency));
        }
        return result;
    }

    private List<String> getVisiblePropsRequiredHelperObjects(ObjectInfo objectInfo) {
        List<String> result = new ArrayList<>();
        for (String key : objectInfo.getVisibleProperties().keySet()) {
            VisibleProperty visibleProperty = objectInfo.getVisibleProperties().get(key);
            if (visibleProperty.getRequiredHelperObjects() != null) {
                result.addAll(visibleProperty.getRequiredHelperObjects());
            }
            if (visibleProperty.getLastSnapshot().getOtherDependencies() != null) {
                for (ObjectInfo finalDependency : visibleProperty.getLastSnapshot().getOtherDependencies()) {
                    result.addAll(getInitRequiredHelperObjects(finalDependency));
                }
            }
            if (visibleProperty.getLastSnapshot().getValue().getObjectInfo() != null) {
                result.addAll(getVisiblePropsRequiredHelperObjects(visibleProperty.getLastSnapshot().getValue().getObjectInfo()));
            }
        }
        return result;
    }
}
