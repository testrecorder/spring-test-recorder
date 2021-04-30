package com.onushi.testrecording.analyzer.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import org.springframework.stereotype.Service;

@Service
public class ObjectCreationAnalyzerService {
    private final ClassInfoService classInfoService;

    public ObjectCreationAnalyzerService(ClassInfoService classInfoService) {
        this.classInfoService = classInfoService;
    }

    public boolean canBeCreatedWithLombokBuilder(Object object) {
        if (object == null) {
            return false;
        } else {
            return classInfoService.canBeCreatedWithLombokBuilder(object.getClass());
        }
    }

    // TODO IB !!!! add more here
}
