package com.onushi.testrecording.codegenerator.object;

import org.springframework.aop.TargetSource;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class CglibService {

    // TODO IB !!!! Write a test for this
    // TODO IB !!!! verify and improve
    public Object getTargetObject(Object object) {
        if (object == null) {
            return null;
        }
        if (object.getClass().getName().contains("$$EnhancerBySpringCGLIB$$")) {
            return extractTargetObject(object);
        } else {
            return object;
        }
    }

    private Object extractTargetObject(Object proxied) {
        try {
            return findSpringTargetSource(proxied).getTarget();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TargetSource findSpringTargetSource(Object proxied) {
        Method[] methods = proxied.getClass().getDeclaredMethods();
        Method targetSourceMethod = findTargetSourceMethod(proxied, methods);
        targetSourceMethod.setAccessible(true);
        try {
            return (TargetSource)targetSourceMethod.invoke(proxied);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method findTargetSourceMethod(Object proxied, Method[] methods) {
        for (Method method : methods) {
            if (method.getName().endsWith("getTargetSource")) {
                return method;
            }
        }
        throw new IllegalStateException(
                "Could not find target source method on proxied object ["
                        + proxied.getClass() + "]");
    }
}
