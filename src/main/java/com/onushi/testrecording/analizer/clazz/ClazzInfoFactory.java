package com.onushi.testrecording.analizer.clazz;

import org.springframework.stereotype.Component;

// TODO IB needed?
@Component
public class ClazzInfoFactory {
    public ClazzInfo getClassInfo(Object object) {
        return new ClazzInfo(object);
    }
}
