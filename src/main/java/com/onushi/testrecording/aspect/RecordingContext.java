package com.onushi.testrecording.aspect;

import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfoBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class RecordingContext {
    private final Set<RecordedMethodRunInfoBuilder> recordedMethodRunInfoBuilderSet = Collections.synchronizedSet(new HashSet<>());

    public Set<RecordedMethodRunInfoBuilder> getMethodRunInfoBuilderSet() {
        return recordedMethodRunInfoBuilderSet;
    }
}
