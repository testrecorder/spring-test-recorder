package com.onushi.testrecording.analizer.classInfo;

import org.springframework.stereotype.Component;

// TODO IB needed?
@Component
public class ClassInfoFactory {
    public ClassInfo getClassInfo(Object object) {
        return new ClassInfo(object);
    }
}
