package com.onushi.testrecording.aspect;

import com.onushi.testrecording.analyzer.methodrun.MethodRunInfoBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class RecordingContext {
    private final Set<MethodRunInfoBuilder> methodRunInfoBuilderSet = Collections.synchronizedSet(new HashSet<>());

    public Set<MethodRunInfoBuilder> getMethodRunInfoBuilderSet() {
        return methodRunInfoBuilderSet;
    }
}
