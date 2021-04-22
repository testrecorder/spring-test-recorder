package com.onushi.testrecording.analizer.methodrun;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import org.springframework.stereotype.Service;

// TODO IB !!!! move from here
@Service
public class MethodRunInfoService {
    private final ClassInfoService classInfoService;

    public MethodRunInfoService(ClassInfoService classInfoService) {
        this.classInfoService = classInfoService;
    }

     public String generateObjectName(MethodRunInfo methodRunInfo, Object object) {
        if (methodRunInfo.getObjectNames().containsKey(object)) {
            return methodRunInfo.getObjectNames().get(object);
        } else {
            String newObjectName = getNewObjectName(methodRunInfo, object);
            methodRunInfo.getObjectNames().put(object, newObjectName);
            return newObjectName;
        }
    }

    private String getNewObjectName(MethodRunInfo methodRunInfo, Object object) {
        String objectNameBase = classInfoService.getObjectNameBase(object);
        int newIndex;
        if (methodRunInfo.getLastIndexForObjectName().containsKey(objectNameBase)) {
            newIndex = methodRunInfo.getLastIndexForObjectName().get(objectNameBase) + 1;
        } else {
            newIndex = 1;
        }
        methodRunInfo.getLastIndexForObjectName().put(objectNameBase, newIndex);
        return objectNameBase + newIndex;
    }
}
