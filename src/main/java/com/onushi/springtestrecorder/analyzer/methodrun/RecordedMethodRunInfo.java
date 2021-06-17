package com.onushi.springtestrecorder.analyzer.methodrun;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
// TODO IB !!!! obsolete
public class RecordedMethodRunInfo {
    // TODO IB !!!! how about the thread?
    protected long threadId;
    protected Object target;
    protected String methodName;
    protected List<Object> arguments;
    protected Class<?> fallBackResultType;
    protected List<DependencyMethodRunInfo> dependencyMethodRuns;
    protected Object result;
    protected Exception exception;
}
