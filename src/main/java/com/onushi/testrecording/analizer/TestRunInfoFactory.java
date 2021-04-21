package com.onushi.testrecording.analizer;

import com.onushi.testrecording.analizer.object.ObjectInfoFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class TestRunInfoFactory {
    private final ObjectInfoFactory objectInfoFactory;

    public TestRunInfoFactory(ObjectInfoFactory objectInfoFactory) {
        this.objectInfoFactory = objectInfoFactory;
    }

    public TestRunInfo getTestRunInfo(MethodInvocationProceedingJoinPoint methodInvocation, Object result) {
        ObjectNameGeneratorImpl objectNameGenerator = new ObjectNameGeneratorImpl();
        return new TestRunInfo(methodInvocation, result, objectNameGenerator, objectInfoFactory);
    }
}
