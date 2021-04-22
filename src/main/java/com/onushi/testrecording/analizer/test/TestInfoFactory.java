package com.onushi.testrecording.analizer.test;

import com.onushi.testrecording.analizer.object.ObjectInfoFactory;
import com.onushi.testrecording.analizer.utils.ClassService;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class TestInfoFactory {
    private final ObjectInfoFactory objectInfoFactory;
    private final ObjectNameService objectNameService;
    private final ClassService classService;

    public TestInfoFactory(ObjectInfoFactory objectInfoFactory, ObjectNameService objectNameService, ClassService classService) {
        this.objectInfoFactory = objectInfoFactory;
        this.objectNameService = objectNameService;
        this.classService = classService;
    }

    public TestInfo getTestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object result) {
        return TestInfo.createTestRunInfo(methodInvocation, result, classService, objectNameService, objectInfoFactory);
    }
}
