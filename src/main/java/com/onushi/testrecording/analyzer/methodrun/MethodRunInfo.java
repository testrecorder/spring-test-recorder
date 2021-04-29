package com.onushi.testrecording.analyzer.methodrun;

import lombok.Builder;
import lombok.Getter;

import java.util.*;


@Getter
@Builder
public class MethodRunInfo {
    protected Object target;
    protected String methodName;
    protected List<Object> arguments;
    protected Class<?> fallBackResultType;
    protected Object result;
    protected Exception exception;

    protected MethodRunInfo() {}

    public MethodRunInfo(Object target, String methodName, List<Object> arguments, Class<?> fallBackResultType, Object result, Exception exception) {
        this.target = target;
        this.methodName = methodName;
        this.arguments = arguments;
        this.fallBackResultType = fallBackResultType;
        this.result = result;
        this.exception = exception;
    }
}
