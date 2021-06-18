package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.springtestrecorder.analyzer.classInfo.ClassInfoService;
import com.onushi.springtestrecorder.codegenerator.object.ObjectInfo;
import com.onushi.springtestrecorder.codegenerator.object.VisibleProperty;
import com.onushi.springtestrecorder.codegenerator.object.VisiblePropertySnapshot;
import com.onushi.springtestrecorder.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TestSideEffectsGeneratorService {
    // TODO IB !!!! remove not needed
    private final ClassInfoService classInfoService;
    private final TestObjectsInitGeneratorService testObjectsInitGeneratorService;
    private final StringService stringService;

    public TestSideEffectsGeneratorService(ClassInfoService classInfoService,
                                      TestObjectsInitGeneratorService testObjectsInitGeneratorService,
                                      StringService stringService) {
        this.classInfoService = classInfoService;
        this.testObjectsInitGeneratorService = testObjectsInitGeneratorService;
        this.stringService = stringService;
    }

    public String getSideEffectsCode(TestGenerator testGenerator) {
        List<ObjectInfo> objectsWithSideEffects = new ArrayList<>();
        for (ObjectInfo objectInfo : testGenerator.getObjectInfoCache().values()) {
            Map<String, VisibleProperty> visibleProperties = objectInfo.getVisibleProperties();
            for (String key : visibleProperties.keySet()) {
                VisibleProperty visibleProperty = visibleProperties.get(key);
                if (visibleProperty.getSnapshots().values().size() > 1) {
                    VisiblePropertySnapshot firstSnapshot = visibleProperty.getFirstSnapshot();
                    VisiblePropertySnapshot lastSnapshot = visibleProperty.getLastSnapshot();
                    if (!firstSnapshot.getValue().isSameValue(lastSnapshot.getValue())) {
                        objectsWithSideEffects.add(objectInfo);
                        continue;
                    }
                }
            }
        }
        if (objectsWithSideEffects.size() > 0) {
            return  "\n" +
                    "        // Side Effects\n";
        } else {
            return "";
        }
    }
}
