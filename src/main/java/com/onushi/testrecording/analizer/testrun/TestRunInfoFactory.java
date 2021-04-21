package com.onushi.testrecording.analizer.testrun;

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
        ObjectNameGenerator objectNameGenerator = new ObjectNameGeneratorImpl();
        return TestRunInfo.createTestRunInfo(methodInvocation, result, objectNameGenerator, objectInfoFactory);
    }
}
