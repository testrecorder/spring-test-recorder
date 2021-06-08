package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import com.onushi.testrecording.codegenerator.template.StringService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestObjectsInitGeneratorService {
    private final StringService stringService;

    public TestObjectsInitGeneratorService(StringService stringService) {
        this.stringService = stringService;
    }

    public String getObjectsInit(List<ObjectInfo> objectInfos) {
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectInfo objectInfo : objectInfos) {
            allObjectsInit.addAll(getObjectsInit(objectInfo));
        }
        return allObjectsInit.stream()
                .map(x -> stringService.addPrefixOnAllLines(x, "        ") + "\n")
                .collect(Collectors.joining(""));
    }

    private List<String> getObjectsInit(ObjectInfo objectInfo) {
        if (objectInfo.isInitAdded()) {
            // to avoid double init
            return new ArrayList<>();
        }
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectInfo dependency : objectInfo.getInitDependencies()) {
            allObjectsInit.addAll(getObjectsInit(dependency));
        }
        if (!objectInfo.getInitCode().equals("")) {
            allObjectsInit.add(objectInfo.getInitCode());
        }
        objectInfo.setInitAdded(true);
        return allObjectsInit;
    }
}
