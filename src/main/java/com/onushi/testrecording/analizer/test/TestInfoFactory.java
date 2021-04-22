package com.onushi.testrecording.analizer.test;

import com.onushi.testrecording.analizer.object.ObjectInfoFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class TestInfoFactory {
    private final ObjectInfoFactory objectInfoFactory;
    private final ObjectNameService objectNameService;

    public TestInfoFactory(ObjectInfoFactory objectInfoFactory, ObjectNameService objectNameService) {
        this.objectInfoFactory = objectInfoFactory;
        this.objectNameService = objectNameService;
    }

    public TestInfo getTestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object result) {
        return TestInfo.createTestRunInfo(methodInvocation, result, objectNameService, objectInfoFactory);
    }
}
