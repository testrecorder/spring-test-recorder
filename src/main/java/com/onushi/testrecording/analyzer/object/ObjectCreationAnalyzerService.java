package com.onushi.testrecording.analyzer.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class ObjectCreationAnalyzerService {
    private final ClassInfoService classInfoService;
    private final ObjectStateReaderService objectStateReaderService;

    public ObjectCreationAnalyzerService(ClassInfoService classInfoService, ObjectStateReaderService objectStateReaderService) {
        this.classInfoService = classInfoService;
        this.objectStateReaderService = objectStateReaderService;
    }

    public boolean canBeCreatedWithLombokBuilder(Object object) {
        if (object == null) {
            return false;
        } else {
            return classInfoService.canBeCreatedWithLombokBuilder(object.getClass());
        }
    }

    public boolean canBeCreatedWithNoArgsConstructor(Object object) {
        if (object == null) {
            return false;
        } else {
            // a object with an int == 0 will say it cannot be initialized this way
            Map<String, Optional<Object>> objectState = objectStateReaderService.getObjectState(object);
            long notNullValuesToSet = objectState.values().stream()
                    .filter(Optional::isPresent)
                    .count();
            return classInfoService.hasPublicNoArgsConstructor(object.getClass()) &&
                    notNullValuesToSet == 0;
        }
    }

    // TODO IB !!!! add more here
}
