package com.onushi.testrecording.analizer.methodrun;

import com.onushi.testrecording.analizer.clazz.ClassService;
import org.springframework.stereotype.Service;

// TODO IB !!!! move from here
@Service
public class MethodRunInfoService {
    private final ClassService classService;

    public MethodRunInfoService(ClassService classService) {
        this.classService = classService;
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
        String objectNameBase = classService.getObjectNameBase(object);
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
