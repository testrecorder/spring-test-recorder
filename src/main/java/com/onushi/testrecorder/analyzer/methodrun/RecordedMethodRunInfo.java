package com.onushi.testrecorder.analyzer.methodrun;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RecordedMethodRunInfo {
    protected Object target;
    protected String methodName;
    protected List<Object> arguments;
    protected Class<?> fallBackResultType;
    protected List<DependencyMethodRunInfo> dependencyMethodRuns;
    protected Object result;
    protected Exception exception;
    protected long threadId;
}
