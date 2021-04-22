package com.onushi.testrecording.analizer.test;

import com.onushi.testrecording.analizer.object.ObjectInfoFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class TestInfoFactory {
    private final ObjectInfoFactory objectInfoFactory;

    public TestInfoFactory(ObjectInfoFactory objectInfoFactory) {
        this.objectInfoFactory = objectInfoFactory;
    }

    public TestInfo getTestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object result) {
        ObjectNameGenerator objectNameGenerator = new ObjectNameGeneratorImpl();
        return TestInfo.createTestRunInfo(methodInvocation, result, objectNameGenerator, objectInfoFactory);
    }
}
