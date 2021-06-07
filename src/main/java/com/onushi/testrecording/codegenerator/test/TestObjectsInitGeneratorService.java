package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.codegenerator.object.ObjectInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestObjectsInitGeneratorService {
    public List<String> getObjectsInit(List<ObjectInfo> objectInfos) {
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectInfo objectInfo : objectInfos) {
            allObjectsInit.addAll(getObjectsInit(objectInfo));
        }
        return allObjectsInit;
    }

    public List<String> getObjectsInit(ObjectInfo objectInfo) {
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
