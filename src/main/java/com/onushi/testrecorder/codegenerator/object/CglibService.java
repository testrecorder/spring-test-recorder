package com.onushi.testrecorder.codegenerator.object;

import org.springframework.aop.TargetSource;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;

@Service
public class CglibService {

    public Object getProxyTargetObject(Object object) throws Exception {
        if (object == null) {
            return null;
        }
        if (object.getClass().getName().contains("$$EnhancerBySpringCGLIB$$")) {
            return extractTargetObject(object);
        } else {
            return object;
        }
    }

    private Object extractTargetObject(Object proxied) throws Exception {
        Method getTargetSourceMethod = Arrays.stream(proxied.getClass().getDeclaredMethods())
            .filter(method -> method.getName().endsWith("getTargetSource"))
            .findFirst().orElse(null);
        if (getTargetSourceMethod == null) {
            throw new IllegalStateException(
                "Could not find target source method on proxied object [" + proxied.getClass() + "]");
        }
        getTargetSourceMethod.setAccessible(true);
        return ((TargetSource)getTargetSourceMethod.invoke(proxied)).getTarget();
    }
}
